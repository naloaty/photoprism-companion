package me.naloaty.photoprism.features.gallery.presentation.list

sealed interface GalleryNews {

    data class OpenPreview(val uid: String) : GalleryNews

}