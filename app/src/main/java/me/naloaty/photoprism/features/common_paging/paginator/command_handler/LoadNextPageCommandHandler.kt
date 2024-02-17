package me.naloaty.photoprism.features.common_paging.paginator.command_handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import me.naloaty.photoprism.features.common_paging.paginator.NextPageFilterStrategy
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorCommand
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorCommandHandler
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorCommandResult
import me.naloaty.photoprism.features.common_paging.paginator.PaginatorEvent

class LoadNextPageCommandHandler<T : Any>(
    private val pageSize: Int,
    private val nextPageFilter: NextPageFilterStrategy<T>,
    private val getFromRemote: (offset: Int) -> List<T>
): PaginatorCommandHandler<T> {
    override fun handle(commands: Flow<PaginatorCommand>): Flow<PaginatorEvent<T>> {
        return commands.filterIsInstance<PaginatorCommand.LoadNextPage<T>>()
            .map {
                try {
                    val nextPage = getFromRemote(pageSize * it.pageNumber)
                    val filteredPage = nextPageFilter(it.currentData, nextPage)
                    PaginatorCommandResult.LoadNextPageResult(filteredPage)
                } catch (e: Throwable) {
                    PaginatorCommandResult.LoadNextPageError(e)
                }
            }
    }
}