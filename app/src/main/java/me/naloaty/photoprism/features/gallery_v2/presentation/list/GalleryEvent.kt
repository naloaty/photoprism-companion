package me.naloaty.photoprism.features.gallery_v2.presentation.list

import me.naloaty.photoprism.common.common_paging.model.PagingError
import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

sealed interface GalleryEvent


sealed interface GalleryUiEvent : GalleryEvent {

    data class OnClickMediaItem(val position: Int): GalleryUiEvent
    data object OnClickRestart : GalleryUiEvent
    data class OnLoadMore(val position: Int) : GalleryUiEvent
    data class OnPerformSearch(val query: GallerySearchQuery) : GalleryUiEvent
}

sealed interface GalleryCommandResult : GalleryEvent {

    data class PerformSearchResult(
        val listState: PagingState<MediaItem>
    ) : GalleryCommandResult

    data class PerformSearchError(
        val error: PagingError
    ) : GalleryCommandResult

}