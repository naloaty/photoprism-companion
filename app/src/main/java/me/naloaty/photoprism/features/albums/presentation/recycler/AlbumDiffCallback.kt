package me.naloaty.photoprism.features.albums.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import me.naloaty.photoprism.features.albums.domain.model.Album

class AlbumDiffCallback: DiffUtil.ItemCallback<Album>() {

    override fun areItemsTheSame(
        oldItem: Album,
        newItem: Album
    ) = oldItem.uid == newItem.uid

    override fun areContentsTheSame(
        oldItem: Album,
        newItem: Album
    ) = oldItem == newItem

}