package me.naloaty.photoprism.features.albums.presentation.list

import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.features.albums.domain.model.Album

data class AlbumState(
    val listState: PagingState<Album> = PagingState.initial()
)