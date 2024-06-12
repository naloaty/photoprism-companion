package me.naloaty.photoprism.features.albums.presentation.search

sealed interface AlbumSearchEvent

sealed interface AlbumSearchUiEvent : AlbumSearchEvent {
    data class OnSearchTextChanged(val text: String) : AlbumSearchUiEvent

    data object OnApplySearch : AlbumSearchUiEvent
    data object OnResetSearch : AlbumSearchUiEvent
}