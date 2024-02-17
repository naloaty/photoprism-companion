package me.naloaty.photoprism.features.common_paging.paginator

sealed interface PaginatorCommand {

    data class LoadNextPage<T>(
        val currentData: List<T>,
        val pageNumber: Int
    ) : PaginatorCommand

}