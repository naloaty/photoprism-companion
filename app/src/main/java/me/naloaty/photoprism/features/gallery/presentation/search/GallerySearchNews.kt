package me.naloaty.photoprism.features.gallery.presentation.search

import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery

sealed interface GallerySearchNews {

    data object HideSearchView : GallerySearchNews
    data class PerformSearch(val query: GallerySearchQuery) : GallerySearchNews
}