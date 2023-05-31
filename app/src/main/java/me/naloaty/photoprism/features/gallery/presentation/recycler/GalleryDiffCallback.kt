package me.naloaty.photoprism.features.gallery.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import me.naloaty.photoprism.features.gallery.presentation.model.GalleryListItem

class GalleryDiffCallback: DiffUtil.ItemCallback<GalleryListItem>() {

    override fun areItemsTheSame(oldItem: GalleryListItem, newItem: GalleryListItem): Boolean {
        val isSameMediaItem = oldItem is GalleryListItem.Media
                && newItem is GalleryListItem.Media
                && oldItem.uid == newItem.uid

        return isSameMediaItem
    }

    override fun areContentsTheSame(
        oldItem: GalleryListItem,
        newItem: GalleryListItem
    ) = oldItem == newItem

}