package me.naloaty.photoprism.features.gallery_v2.presentation

import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

data class GalleryState(
    val listItems: List<MediaItem>
)