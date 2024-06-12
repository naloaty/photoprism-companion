package me.naloaty.photoprism.features.gallery.presentation.search

sealed interface GallerySearchCommand {

    data class GetAlbum(val albumUid: String) : GallerySearchCommand
}