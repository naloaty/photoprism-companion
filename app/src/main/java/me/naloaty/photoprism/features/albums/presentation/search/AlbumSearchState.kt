package me.naloaty.photoprism.features.albums.presentation.search

import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.util.EMPTY_STRING

data class AlbumSearchState(
    val album: Album? = null,
    val queryText: String = EMPTY_STRING,
    val currentQuery: AlbumSearchQuery? = null
)