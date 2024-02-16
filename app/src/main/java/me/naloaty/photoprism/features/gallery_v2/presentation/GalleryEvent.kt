package me.naloaty.photoprism.features.gallery_v2.presentation

import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

sealed interface GalleryEvent


sealed interface GalleryUiEvent : GalleryEvent {

    data class OnClickMediaItem(val position: Int): GalleryUiEvent
}

sealed interface GalleryCommandResult : GalleryEvent {

    data class PerformSearchResult(
        val items: List<MediaItem>
    ) : GalleryCommandResult

}