package me.naloaty.photoprism.features.albums.presentation.search

import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery

sealed interface AlbumSearchNews {

    data object HideSearchView : AlbumSearchNews
    data class PerformSearch(val query: AlbumSearchQuery) : AlbumSearchNews
}