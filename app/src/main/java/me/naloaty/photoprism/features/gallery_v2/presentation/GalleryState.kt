package me.naloaty.photoprism.features.gallery_v2.presentation

import me.naloaty.photoprism.features.common_paging.model.PagingState
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

data class GalleryState(
    val listState: PagingState<MediaItem> = PagingState.initial()
)