package me.naloaty.photoprism.common.common_paging.paginator

import me.naloaty.photoprism.common.common_paging.model.PagingError
import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.common.common_paging.model.data
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Next
import ru.tinkoff.kotea.core.Update

typealias PaginatorCommandHandler<T> = CommandsFlowHandler<PaginatorCommand, PaginatorEvent<T>>

class PaginatorUpdate<T : Any> :
    Update<PaginatorState<T>, PaginatorEvent<T>, PaginatorCommand, PagingError> {

    override fun update(
        state: PaginatorState<T>,
        event: PaginatorEvent<T>
    ): Next<PaginatorState<T>, PaginatorCommand, PagingError> {
        return Next(
            state = state.copy(pagingState = updateState(state.pagingState, event)),
            commands = updateCommands(state.pagingState, event),
            news = updateNews(state.pagingState, event)
        )
    }

    private fun updateState(state: PagingState<T>, event: PaginatorEvent<T>): PagingState<T> {
        return when (event) {
            is PaginatorUiEvent.Refresh -> when (state) {
                is PagingState.Empty -> PagingState.EmptyProgress
                is PagingState.EmptyError -> PagingState.EmptyProgress
                is PagingState.Data -> PagingState.Refresh(state.data)
                is PagingState.NewPageProgress -> PagingState.Refresh(state.data)
                is PagingState.NewPageError -> PagingState.Refresh(state.data)
                is PagingState.FullData -> PagingState.Refresh(state.data)
                else -> state
            }
            is PaginatorUiEvent.Restart -> when (state) {
                is PagingState.Empty -> PagingState.EmptyProgress
                is PagingState.EmptyError -> PagingState.EmptyProgress
                is PagingState.Data -> PagingState.EmptyProgress
                is PagingState.Refresh -> PagingState.EmptyProgress
                is PagingState.NewPageProgress -> PagingState.EmptyProgress
                is PagingState.NewPageError -> PagingState.EmptyProgress
                is PagingState.FullData -> PagingState.EmptyProgress
                else -> state
            }
            is PaginatorUiEvent.LoadMore -> when (state) {
                is PagingState.Data -> PagingState.NewPageProgress(state.data)
                is PagingState.NewPageError -> PagingState.NewPageProgress(state.data)
                else -> state
            }
            is PaginatorCommandResult.LoadNextPageResult -> when (state) {
                is PagingState.EmptyProgress -> {
                    if (event.items.isEmpty()) {
                        PagingState.Empty
                    } else {
                        PagingState.Data(event.items)
                    }
                }
                is PagingState.Refresh -> {
                    if (event.items.isEmpty()) {
                        PagingState.Empty
                    } else {
                        PagingState.Data(event.items)
                    }
                }
                is PagingState.NewPageProgress -> {
                    if (event.items.isEmpty()) {
                        PagingState.FullData(state.data)
                    } else {
                        PagingState.Data(state.data + event.items)
                    }
                }
                else -> state
            }
            is PaginatorCommandResult.LoadNextPageError -> when (state) {
                is PagingState.EmptyProgress -> PagingState.EmptyError(event.error)
                is PagingState.Refresh -> PagingState.Data(state.data)
                is PagingState.NewPageProgress -> PagingState.NewPageError(state.data)
                else -> state
            }
        }
    }

    private fun updateCommands(
        state: PagingState<T>,
        event: PaginatorEvent<T>
    ): List<PaginatorCommand> {
        return when (event) {
            is PaginatorUiEvent.Restart,
            is PaginatorUiEvent.Refresh -> listOf(
                PaginatorCommand.LoadNextPage(state.data,  isRefresh = true)
            )
            is PaginatorUiEvent.LoadMore -> when (state) {
                is PagingState.Data -> listOf(
                    PaginatorCommand.LoadNextPage(state.data)
                )
                is PagingState.NewPageError -> listOf(
                    PaginatorCommand.LoadNextPage(state.data)
                )
                else -> emptyList()
            }
            else -> emptyList()
        }
    }

    private fun updateNews(
        state: PagingState<T>,
        event: PaginatorEvent<T>
    ): List<PagingError> {
        return when (event) {
            is PaginatorCommandResult.LoadNextPageError -> when (state) {
                is PagingState.Refresh,
                is PagingState.NewPageProgress -> listOf(
                    PagingError.PageLoadError(event.error)
                )
                else -> emptyList()
            }
            else -> emptyList()
        }
    }
}