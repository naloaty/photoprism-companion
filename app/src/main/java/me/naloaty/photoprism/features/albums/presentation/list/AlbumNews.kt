package me.naloaty.photoprism.features.albums.presentation.list

sealed interface AlbumNews {

    data class OpenGalleryView(val albumUid: String) : AlbumNews
}