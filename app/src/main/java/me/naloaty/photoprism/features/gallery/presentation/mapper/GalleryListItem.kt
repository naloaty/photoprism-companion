package me.naloaty.photoprism.features.gallery.presentation.mapper

import me.naloaty.photoprism.R
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.presentation.model.GalleryListItem

fun MediaItem.toGalleryListItem(): GalleryListItem =
    GalleryListItem.Media(
        uid = this.uid,
        typeIcon = when (this) {
            is MediaItem.Vector -> R.drawable.ic_vector
            is MediaItem.Animated -> R.drawable.ic_animation
            is MediaItem.Text -> R.drawable.ic_text
            is MediaItem.Video -> R.drawable.ic_video
            is MediaItem.Live -> R.drawable.ic_live_photo
            is MediaItem.Unknown -> R.drawable.ic_unknown
            else -> null
        },
        title = this.title,
        thumbnailUrl = this.smallThumbnailUrl
    )