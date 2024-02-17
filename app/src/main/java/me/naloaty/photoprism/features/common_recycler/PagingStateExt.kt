package me.naloaty.photoprism.features.common_recycler

import me.naloaty.photoprism.features.common_paging.model.PagingState
import me.naloaty.photoprism.features.common_paging.model.PagingState.FullData
import me.naloaty.photoprism.features.common_recycler.model.CommonEmptyItem
import me.naloaty.photoprism.features.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.features.common_recycler.model.CommonLoadingItem
import me.naloaty.photoprism.features.common_recycler.model.CommonNextPageErrorItem
import me.naloaty.photoprism.features.common_recycler.model.CommonNextPageLoadingItem
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

fun <T : Any> PagingState<T>.asStateItems(
    contentMapper: (List<T>) -> List<ViewTyped>,
    loadingItems: () -> List<ViewTyped> = { listOf(CommonLoadingItem) },
    emptyItems: () -> List<ViewTyped> = { listOf(CommonEmptyItem) },
    errorItems: () -> List<ViewTyped> = { listOf(CommonErrorItem) },
    nextPageLoadingItem: () -> ViewTyped = { CommonNextPageLoadingItem },
    nextPageErrorItem: () -> ViewTyped = { CommonNextPageErrorItem }
): List<ViewTyped> {
    return when (this) {
        is PagingState.Empty -> emptyItems()
        is PagingState.EmptyProgress -> loadingItems()
        is PagingState.EmptyError -> errorItems()
        is PagingState.Data -> contentMapper(data)
        is PagingState.Refresh -> contentMapper(data)
        is PagingState.NewPageProgress -> contentMapper(data) + nextPageLoadingItem()
        is FullData -> contentMapper(data)
        is PagingState.NewPageError -> contentMapper(data) + nextPageErrorItem()
    }
}