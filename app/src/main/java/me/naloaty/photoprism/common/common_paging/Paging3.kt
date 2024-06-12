package me.naloaty.photoprism.common.common_paging

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.common.common_paging.model.PagingError
import me.naloaty.photoprism.common.common_paging.model.PagingState

fun <T : Any, R : Any> offsetPaginatorPaging3(
    refreshEvents: Flow<Any>,
    restartEvents: Flow<Any>,
    loadMoreEvents: Flow<Int>,
    pagingDataFlow: Flow<PagingData<T>>,
    diffCallback: DiffUtil.ItemCallback<T>,
    mapState: (state: PagingState<T>) -> R,
    mapError: (error: PagingError) -> R
): Flow<R> {
    return Paging3PaginatorImpl(
        refreshEvents = refreshEvents,
        restartEvents = restartEvents,
        loadMoreEvents = loadMoreEvents,
        pagingDataFlow = pagingDataFlow,
        diffCallback = diffCallback,
        mapState = mapState,
        mapError = mapError
    )
}

private class Paging3PaginatorImpl<T : Any, R : Any>(
    private val refreshEvents: Flow<Any>,
    private val restartEvents: Flow<Any>,
    private val loadMoreEvents: Flow<Int>,
    private val pagingDataFlow: Flow<PagingData<T>>,
    private val diffCallback: DiffUtil.ItemCallback<T>,
    private val mapState: (state: PagingState<T>) -> R,
    private val mapError: (error: PagingError) -> R
) : Flow<R> {

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
    }

    private val differ = AsyncPagingDataDiffer(
        diffCallback = diffCallback,
        updateCallback = noopListUpdateCallback,
        mainDispatcher = Dispatchers.Default,
        workerDispatcher = Dispatchers.Default
    )

    private val data: List<T>
        get() = differ.snapshot().items

    private val state = MutableStateFlow<PagingState<T>>(PagingState.EmptyProgress)
    private val error = MutableSharedFlow<PagingError>()

    override suspend fun collect(collector: FlowCollector<R>) {
        coroutineScope {
            launch { pagingDataFlow.collectLatest { differ.submitData(it) } }
            launch { refreshEvents.collect { differ.refresh() } }
            launch { restartEvents.collect { differ.retry() } }
            launch {
                loadMoreEvents.collect { position ->
                    if (position in 0 until differ.itemCount) {
                        differ.getItem(position)
                    }
                }
            }
            launch { differ.loadStateFlow.collectLatest { updateState(it) } }
            launch { state.collectLatest { collector.emit(mapState(it)) } }
            launch { error.collect { collector.emit(mapError(it)) } }
        }
    }

    private fun updateState(states: CombinedLoadStates) {
        val mediator = states.mediator
            ?: throw IllegalStateException("Mediator is required for Paging3 paginator")

        val sourceRefresh = states.source.refresh
        val mediatorRefresh = mediator.refresh
        val mediatorAppend = mediator.append
        val mediatorPrepend = mediator.prepend

        if (differ.itemCount > 0) {
            when {
                sourceRefresh is Loading -> state { PagingState.Refresh(data) }
                sourceRefresh is Error -> {
                    error { PagingError.PageLoadError(sourceRefresh.error) }
                    state { PagingState.Data(data) }
                }
                mediatorRefresh is Loading -> state { PagingState.Refresh(data) }
                mediatorRefresh is Error -> {
                    error { PagingError.PageLoadError(mediatorRefresh.error) }
                    state { PagingState.Data(data) }
                }

                mediatorAppend is Loading -> state { PagingState.NewPageProgress(data) }
                mediatorAppend is Error -> state { PagingState.NewPageError(data) }
                mediatorAppend is NotLoading && mediatorAppend.endOfPaginationReached -> {
                    state { PagingState.FullData(data) }
                }
                else -> state { PagingState.Data(data) }
            }
        } else {
            when {
                sourceRefresh is Loading -> state { PagingState.EmptyProgress }
                sourceRefresh is Error -> state { PagingState.EmptyError(sourceRefresh.error) }
                mediatorRefresh is Loading -> state { PagingState.Refresh(data) }
                mediatorRefresh is Error -> state { PagingState.EmptyError(mediatorRefresh.error) }
                mediatorPrepend is Loading -> state { PagingState.EmptyProgress }
                mediatorAppend is Loading -> state { PagingState.EmptyProgress }
                else -> state { PagingState.Empty }
            }
        }
    }

    private inline fun state(block: () -> PagingState<T>) {
        state.tryEmit(block())
    }

    private inline fun error(block: () -> PagingError) {
        error.tryEmit(block())
    }
}