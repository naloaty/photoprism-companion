package me.naloaty.photoprism.features.gallery_v2.presentation

interface GalleryCommand {

    data object Refresh : GalleryCommand
    data object Restart : GalleryCommand
    data object LoadMore : GalleryCommand

    data class PerformSearch(val query: String) : GalleryCommand

}