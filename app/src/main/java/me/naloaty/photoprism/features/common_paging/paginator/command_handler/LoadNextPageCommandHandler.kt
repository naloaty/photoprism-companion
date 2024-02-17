package me.naloaty.photoprism.features.common_paging.paginator.command_handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import me.naloaty.photoprism.features.common_paging.paginator.NextPageFilterStrategy
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorCommand
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorCommandHandler
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorCommandResult
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorEvent
import me.naloaty.photoprism.features.common_paging.paginator.RemotePageLoader

class LoadNextPageCommandHandler<T : Any>(
    private val nextPageFilterStrategy: NextPageFilterStrategy<T>,
    private val getFromRemote: RemotePageLoader<T>
): PaginatorCommandHandler<T> {
    override fun handle(commands: Flow<PaginatorCommand>): Flow<PaginatorEvent<T>> {
        return commands.filterIsInstance<PaginatorCommand.LoadNextPage<T>>()
            .map { command ->
                try {
                    val offset = if (command.isRefresh) 0 else command.currentData.size
                    val nextPage = getFromRemote(offset)
                    val filteredPage = nextPageFilterStrategy(command.currentData, nextPage)
                    PaginatorCommandResult.LoadNextPageResult(filteredPage)
                } catch (e: Throwable) {
                    PaginatorCommandResult.LoadNextPageError(e)
                }
            }
    }
}