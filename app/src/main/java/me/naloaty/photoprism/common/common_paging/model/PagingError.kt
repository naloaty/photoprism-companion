package me.naloaty.photoprism.common.common_paging.model

sealed interface PagingError {

    data class PageLoadError(val error: Throwable) : PagingError
}