package me.naloaty.photoprism.features.gallery.presentation.search

import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.util.EMPTY_STRING

data class GallerySearchState(
    val album: Album? = null,
    val queryText: String = EMPTY_STRING,
    val currentQuery: GallerySearchQuery? = null
)