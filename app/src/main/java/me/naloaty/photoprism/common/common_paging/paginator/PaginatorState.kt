package me.naloaty.photoprism.common.common_paging.paginator

import me.naloaty.photoprism.common.common_paging.model.PagingState

data class PaginatorState<out T : Any>(
    val pagingState: PagingState<T>
)