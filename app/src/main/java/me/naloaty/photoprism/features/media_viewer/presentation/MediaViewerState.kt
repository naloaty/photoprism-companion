package me.naloaty.photoprism.features.media_viewer.presentation

import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

data class MediaViewerState(
    val position: Int = 0,
    val items: List<MediaItem> = emptyList(),
)