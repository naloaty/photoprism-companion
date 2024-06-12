package me.naloaty.photoprism.features.gallery.presentation.list

import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery

interface GalleryCommand {

    data object Refresh : GalleryCommand
    data object Restart : GalleryCommand
    data class LoadMore(val position: Int) : GalleryCommand
    data class PerformSearch(val query: GallerySearchQuery) : GalleryCommand

}