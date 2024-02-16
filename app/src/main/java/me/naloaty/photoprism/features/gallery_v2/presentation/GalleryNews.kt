package me.naloaty.photoprism.features.gallery_v2.presentation

sealed interface GalleryNews {

    data class OpenPreview(val position: Int) : GalleryNews

}