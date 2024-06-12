package me.naloaty.photoprism.features.gallery.presentation.list

import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

data class GalleryState(
    val listState: PagingState<MediaItem> = PagingState.initial()
)