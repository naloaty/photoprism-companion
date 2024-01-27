package me.naloaty.photoprism.features.gallery.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LiGalleryMediaItemBinding
import me.naloaty.photoprism.features.gallery.presentation.model.GalleryListItem

class GalleryAdapter : PagingDataAdapter<GalleryListItem, RecyclerView.ViewHolder>(
    GalleryDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.li_gallery_media_item -> MediaItemViewHolder(
                LiGalleryMediaItemBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalStateException("Unknown view type '$viewType'")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MediaItemViewHolder -> {
                holder.dispose()
                holder.bind(getItem(position) as GalleryListItem.Media?)
            }

            else -> throw IllegalStateException("Unknown view holder type '${holder::class.simpleName}'")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (peek(position)) {
            is GalleryListItem.Media -> R.layout.li_gallery_media_item
            null -> R.layout.li_gallery_media_item
        }
    }

}