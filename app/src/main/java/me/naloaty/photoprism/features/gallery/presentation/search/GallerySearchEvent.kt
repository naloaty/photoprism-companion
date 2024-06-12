package me.naloaty.photoprism.features.gallery.presentation.search

import me.naloaty.photoprism.features.albums.domain.model.Album

sealed interface GallerySearchEvent

sealed interface GallerySearchUiEvent : GallerySearchEvent {
    data class OnSearchTextChanged(val text: String) : GallerySearchUiEvent

    data object OnApplySearch : GallerySearchUiEvent
    data object OnResetSearch : GallerySearchUiEvent
}

sealed interface GallerySearchCommandResult : GallerySearchEvent {

    data class GetAlbumResult(val album: Album) : GallerySearchCommandResult
}