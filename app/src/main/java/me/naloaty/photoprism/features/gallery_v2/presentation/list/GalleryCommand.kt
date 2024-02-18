package me.naloaty.photoprism.features.gallery_v2.presentation.list

interface GalleryCommand {

    data object Refresh : GalleryCommand
    data object Restart : GalleryCommand
    data object LoadMore : GalleryCommand

    data class PerformSearch(val query: String) : GalleryCommand

}