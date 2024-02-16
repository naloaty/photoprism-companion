package me.naloaty.photoprism.features.media_viewer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LayoutDefaultPreviewBinding
import me.naloaty.photoprism.databinding.LayoutPicturePreviewBinding
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.ViewableAsImage

class MediaViewerAdapter : PagingDataAdapter<MediaItem, RecyclerView.ViewHolder>(
    MediaItemDiffCallback()
) {

    var onItemClickListener: ((MediaItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.layout_picture_preview -> ViewableAsImageViewHolder(
                binding = LayoutPicturePreviewBinding.inflate(inflater, parent, false),
                onClickListener = { onItemClickListener?.invoke(it) },
                onLoadCompletedListener = {  }
            )

            R.layout.layout_default_preview -> DefaultViewHolder(
                binding = LayoutDefaultPreviewBinding.inflate(inflater, parent, false),
                onClickListener = { onItemClickListener?.invoke(it) },
                onLoadCompletedListener = {  }
            )

            else -> throw IllegalStateException("Unknown view type '$viewType'")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewableAsImageViewHolder -> {
                holder.bind(position, getItem(position) as? ViewableAsImage)
            }

            is DefaultViewHolder -> {
                holder.bind(position, getItem(position))
            }

            else -> throw IllegalStateException("Unknown view holder type '${holder::class.simpleName}'")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (peek(position)) {
            is ViewableAsImage -> R.layout.layout_picture_preview
            else -> R.layout.layout_default_preview
        }
    }

}