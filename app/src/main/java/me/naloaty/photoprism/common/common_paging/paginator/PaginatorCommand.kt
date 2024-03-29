package me.naloaty.photoprism.common.common_paging.paginator

sealed interface PaginatorCommand {

    data class LoadNextPage<T>(
        val currentData: List<T>,
        val isRefresh: Boolean = false
    ) : PaginatorCommand

}