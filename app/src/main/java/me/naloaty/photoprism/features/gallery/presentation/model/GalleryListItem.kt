package me.naloaty.photoprism.features.gallery.presentation.model

import androidx.annotation.DrawableRes

sealed interface GalleryListItem {

    data class Media(
        val uid: String,
        @DrawableRes
        val typeIcon: Int?,
        val title: String,
        val thumbnailUrl: String
    ) : GalleryListItem

}