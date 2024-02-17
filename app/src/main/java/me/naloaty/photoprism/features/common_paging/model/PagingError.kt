package me.naloaty.photoprism.features.common_paging.model

sealed interface PagingError {

    data class PageLoadError(val error: Throwable) : PagingError
}