package me.naloaty.photoprism.features.gallery_v2.presentation.search

sealed interface GallerySearchEvent

sealed interface GallerySearchUiEvent : GallerySearchEvent {
    data class OnSearchTextChanged(val text: String) : GallerySearchUiEvent

    data object OnApplySearch : GallerySearchUiEvent
    data object OnResetSearch : GallerySearchUiEvent
}