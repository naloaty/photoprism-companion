package me.naloaty.photoprism.features.albums.presentation.list

import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery

sealed interface AlbumCommand {

    data object Refresh : AlbumCommand
    data object Restart : AlbumCommand
    data class LoadMore(val position: Int) : AlbumCommand
    data class PerformSearch(val query: AlbumSearchQuery) : AlbumCommand

}