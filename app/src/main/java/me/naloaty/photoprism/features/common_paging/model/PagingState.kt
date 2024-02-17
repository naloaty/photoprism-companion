package me.naloaty.photoprism.features.common_paging.model

import me.naloaty.photoprism.features.common_paging.model.PagingState.FullData

sealed interface PagingState<out T : Any> {

    data object Empty : PagingState<Nothing>
    data object EmptyProgress : PagingState<Nothing>
    data class EmptyError(val error: Throwable) : PagingState<Nothing>
    data class Data<out T : Any>(val pageCount: Int, val data: List<T>) : PagingState<T>
    data class Refresh<out T : Any>(val pageCount: Int, val data: List<T>) : PagingState<T>
    data class NewPageProgress<out T : Any>(val pageCount: Int, val data: List<T>) : PagingState<T>
    data class FullData<out T : Any>(val pageCount: Int, val data: List<T>) : PagingState<T>

}

val <T : Any> PagingState<T>.data: List<T>
    get() = when (this) {
        is PagingState.Empty -> emptyList()
        is PagingState.EmptyProgress -> emptyList()
        is PagingState.EmptyError -> emptyList()
        is PagingState.Data -> data
        is PagingState.Refresh -> data
        is PagingState.NewPageProgress -> data
        is FullData -> data
    }