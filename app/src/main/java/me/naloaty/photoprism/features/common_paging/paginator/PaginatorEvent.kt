package me.naloaty.photoprism.features.common_paging.paginator

sealed interface PaginatorEvent<out T : Any>

sealed interface PaginatorUiEvent<out T : Any> : PaginatorEvent<T> {
    data object Refresh : PaginatorUiEvent<Nothing>
    data object Restart : PaginatorUiEvent<Nothing>
    data object LoadMore : PaginatorUiEvent<Nothing>
}

sealed interface PaginatorCommandResult<out T : Any> : PaginatorEvent<T> {

    data class LoadNextPageResult<out T : Any>(
        val items: List<T>
    ) : PaginatorCommandResult<T>

    data class LoadNextPageError<out T : Any>(
        val error: Throwable
    ) : PaginatorCommandResult<T>
}