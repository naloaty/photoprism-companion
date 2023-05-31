package me.naloaty.photoprism.features.gallery.presentation.recycler

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LiGalleryMediaItemBinding
import me.naloaty.photoprism.features.gallery.presentation.model.GalleryListItem

class MediaItemViewHolder(
    private val binding: LiGalleryMediaItemBinding
) : ViewHolder(binding.root) {

    private var thumbnailTarget: Target<*>? = null

    private fun bindAsPlaceholder() = with(binding) {
        ivItemType.isVisible = false
        ivItemThumbnail.setImageResource(R.color.transparent)
    }

    private fun bindAsRegularItem(item: GalleryListItem.Media) = with(binding) {
        if (item.typeIcon != null) {
            ivItemType.isVisible = true
            ivItemType.setImageResource(item.typeIcon)
        } else {
            ivItemType.isVisible = false
        }

        ivItemThumbnail.contentDescription = item.title

        thumbnailTarget = Glide
            .with(ivItemThumbnail.context)
            .load(item.thumbnailUrl)
            .centerCrop()
            .placeholder(R.color.empty_image)
            .into(ivItemThumbnail)
    }

    fun bind(item: GalleryListItem.Media?): Unit = with(binding) {
        if (item == null)
            bindAsPlaceholder()
        else
            bindAsRegularItem(item)
    }

    fun dispose(): Unit = with(binding) {
        thumbnailTarget?.let {
            Glide.with(ivItemThumbnail.context)
                .clear(thumbnailTarget)

            thumbnailTarget = null
        }
    }
}