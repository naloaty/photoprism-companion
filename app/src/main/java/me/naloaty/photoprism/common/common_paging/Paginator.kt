package me.naloaty.photoprism.common.common_paging

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import me.naloaty.photoprism.common.common_paging.model.PagingError
import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.common.common_paging.paginator.NextPageFilterStrategy
import me.naloaty.photoprism.common.common_paging.paginator.PaginatorCommand
import me.naloaty.photoprism.common.common_paging.paginator.PaginatorState
import me.naloaty.photoprism.common.common_paging.paginator.PaginatorUiEvent
import me.naloaty.photoprism.common.common_paging.paginator.PaginatorUpdate
import me.naloaty.photoprism.common.common_paging.paginator.RemotePageLoader
import me.naloaty.photoprism.common.common_paging.paginator.command_handler.LoadNextPageCommandHandler
import ru.tinkoff.kotea.core.KoteaStore


fun <T : Any, R: Any> offsetPaginator(
    refreshEvents: Flow<Any>,
    restartEvents: Flow<Any>,
    loadMoreEvents: Flow<Any>,
    nextPageFilterStrategy: NextPageFilterStrategy<T>,
    getFromRemote: RemotePageLoader<T>,
    mapState: (state: PagingState<T>) -> R,
    mapError: (error: PagingError) -> R
): Flow<R> {
    return PaginatorImpl(
        refreshEvents = refreshEvents,
        restartEvents = restartEvents,
        loadMoreEvents = loadMoreEvents,
        nextPageFilterStrategy = nextPageFilterStrategy,
        getFromRemote = getFromRemote,
        mapState = mapState,
        mapError = mapError
    )
}


private class PaginatorImpl<out T : Any, R: Any>(
    private val refreshEvents: Flow<Any>,
    private val restartEvents: Flow<Any>,
    private val loadMoreEvents: Flow<Any>,
    private val nextPageFilterStrategy: NextPageFilterStrategy<T>,
    private val getFromRemote: RemotePageLoader<T>,
    private val mapState: (state: PagingState<T>) -> R,
    private val mapError: (error: PagingError) -> R
): Flow<R> {
    override suspend fun collect(collector: FlowCollector<R>) {
        val store = KoteaStore(
            initialState = PaginatorState(
                pagingState = PagingState.EmptyProgress
            ),
            initialCommands = listOf(
                PaginatorCommand.LoadNextPage<T>(emptyList())
            ),
            commandsFlowHandlers = listOf(
                LoadNextPageCommandHandler(nextPageFilterStrategy, getFromRemote)
            ),
            update = PaginatorUpdate()
        )

        coroutineScope {
            store.launchIn(this)
            launch {
                merge(
                    refreshEvents.map { PaginatorUiEvent.Refresh },
                    restartEvents.map { PaginatorUiEvent.Restart },
                    loadMoreEvents.map { PaginatorUiEvent.LoadMore }
                ).collect(store::dispatch)
            }
            launch {
                store.news.map { mapError(it) }
                    .collect { collector.emit(it) }
            }
            launch {
                store.state.map { mapState(it.pagingState) }
                    .collect { collector.emit(it) }
            }
        }
    }
}