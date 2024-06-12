package me.naloaty.photoprism.common.common_recycler

import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.common.common_recycler.model.CommonEmptyItem
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageErrorItem
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageLoadingItem
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

inline fun <T : Any> PagingState<T>.asStateItems(
    noinline contentMapper: (List<T>) -> List<ViewTyped>,
    crossinline loadingItems: () -> List<ViewTyped> = { listOf(CommonNextPageLoadingItem) },
    crossinline emptyItems: () -> List<ViewTyped> = { listOf(CommonEmptyItem) },
    crossinline errorItems: () -> List<ViewTyped> = { listOf(CommonErrorItem) },
    crossinline nextPageLoadingItem: () -> ViewTyped = { CommonNextPageLoadingItem },
    crossinline nextPageErrorItem: () -> ViewTyped = { CommonNextPageErrorItem }
): List<ViewTyped> {
    return when (this) {
        is PagingState.Empty -> emptyItems()
        is PagingState.EmptyProgress -> loadingItems()
        is PagingState.EmptyError -> errorItems()
        is PagingState.Data -> contentMapper(data)
        is PagingState.Refresh -> contentMapper(data)
        is PagingState.NewPageProgress -> contentMapper(data) + nextPageLoadingItem()
        is PagingState.FullData -> contentMapper(data)
        is PagingState.NewPageError -> contentMapper(data) + nextPageErrorItem()
    }
}