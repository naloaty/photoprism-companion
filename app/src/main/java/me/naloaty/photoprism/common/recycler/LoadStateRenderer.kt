package me.naloaty.photoprism.common.recycler

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import me.naloaty.photoprism.databinding.LayoutCommonErrorBinding
import timber.log.Timber

private val TRANSITION_DURATION = 150L

class LoadStateRenderer(
    private val root: ViewGroup,
    private val emptyView: View,
    private val loadingView: View,
    private val errorView: View,
    private val contentView: View,
    private val onFallbackToCache: () -> Unit
) {

    private val state = MutableStateFlow(State.LOADING)


    fun update(states: CombinedLoadStates, itemCount: Int) {
        val mediator = checkNotNull(states.mediator) {
            "LoadStateManager does not support paging setup without mediator"
        }

        val source = states.source

        // Room делает refresh при любом изменении
        val cacheIsUpdating = source.refresh.isLoading
        val cacheIsIdle = source.refresh.isNotLoading
        val remoteAtTheEnd = mediator.append.isNotLoading && mediator.append.endOfPaginationReached
        val remoteError = mediator.refresh.isError || mediator.append.isError

        if (itemCount > 0) {
            if (remoteError) {
                state.tryEmit(State.CACHE)
            } else {
                state.tryEmit(State.CONTENT)
            }
        } else {
            when {
                cacheIsUpdating -> state.tryEmit(State.LOADING)
                cacheIsIdle && remoteAtTheEnd -> state.tryEmit(State.EMPTY)
                cacheIsIdle && remoteError -> state.tryEmit(State.ERROR)
            }
        }
    }

    @OptIn(FlowPreview::class)
    suspend fun render() {
        state.debounce(TRANSITION_DURATION).collectLatest { state ->
            when (state) {
                State.LOADING -> renderLoadingState()
                State.CONTENT -> renderContentState()
                State.EMPTY -> renderEmptyState()
                State.ERROR -> renderErrorState()
                State.CACHE -> {
                    onFallbackToCache()
                    renderContentState()
                }
            }
        }
    }

    private val LoadState.isError: Boolean
        get() = this is LoadState.Error

    private val LoadState.isLoading: Boolean
        get() = this is LoadState.Loading

    private val LoadState.isNotLoading: Boolean
        get() = this is LoadState.NotLoading

    private fun renderEmptyState() {
        Timber.d("Empty state")
        prepareAnimation()
        emptyView.isVisible = true
        loadingView.isVisible = false
        errorView.isVisible = false
        contentView.isVisible = false
    }

    private fun renderContentState() {
        Timber.d("Content state")
        prepareAnimation()
        emptyView.isVisible = false
        loadingView.isVisible = false
        errorView.isVisible = false
        contentView.isVisible = true
    }

    private fun renderLoadingState() {
        Timber.d("Loading state")
        prepareAnimation()
        emptyView.isVisible = false
        loadingView.isVisible = true
        errorView.isVisible = false
        contentView.isVisible = false
    }

    private fun renderErrorState() {
        Timber.d("Error state")
        prepareAnimation()
        emptyView.isVisible = false
        loadingView.isVisible = false
        errorView.isVisible = true
        contentView.isVisible = false
    }

    private fun prepareAnimation() {
        TransitionManager.endTransitions(root)

        val transition = AutoTransition().apply {
            duration = TRANSITION_DURATION
            addTarget(emptyView)
            addTarget(loadingView)
            addTarget(errorView)
            addTarget(contentView)
        }

        TransitionManager.beginDelayedTransition(root, transition)
    }

    private enum class State {
        LOADING,
        CONTENT,
        EMPTY,
        ERROR,
        CACHE
    }

}

fun initCommonError(binding: LayoutCommonErrorBinding, onRetry: () -> Unit) {
    binding.btnRetry.setOnClickListener { onRetry() }
}