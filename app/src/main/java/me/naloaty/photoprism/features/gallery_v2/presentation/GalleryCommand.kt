package me.naloaty.photoprism.features.gallery_v2.presentation

interface GalleryCommand {

    data class PerformSearch(val query: String) : GalleryCommand

}