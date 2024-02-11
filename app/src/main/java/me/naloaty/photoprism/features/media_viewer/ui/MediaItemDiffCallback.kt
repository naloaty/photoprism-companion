package me.naloaty.photoprism.features.media_viewer.ui

import androidx.recyclerview.widget.DiffUtil
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

class MediaItemDiffCallback : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem == newItem
    }

}