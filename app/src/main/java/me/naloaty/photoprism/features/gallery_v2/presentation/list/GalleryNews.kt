package me.naloaty.photoprism.features.gallery_v2.presentation.list

sealed interface GalleryNews {

    data class OpenPreview(val position: Int) : GalleryNews

}