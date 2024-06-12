package me.naloaty.photoprism.features.gallery.ui.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.Target
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.ItemGalleryMediaThumbnailBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

data class MediaThumbnailItem(
    override val uid: String,
    val title: String,
    val thumbnailUrl: String,
    @DrawableRes val typeIcon: Int? = null,
) : ViewTyped {
    override val viewType: Int = R.layout.item_gallery_media_thumbnail
}


class MediaThumbnailItemViewHolder(
    view: View,
    clicks: TiRecyclerClickListener,
    private val requestManager: RequestManager
) : BaseViewHolder<MediaThumbnailItem>(view) {

    private val binding = ItemGalleryMediaThumbnailBinding.bind(view)
    private var thumbnailTarget: Target<*>? = null

    init {
        clicks.accept(binding.cvThumbnail, this)
    }

    override fun bind(item: MediaThumbnailItem) = with(binding) {
        if (item.typeIcon != null) {
            ivItemType.isVisible = true
            ivItemType.setImageResource(item.typeIcon)
        } else {
            ivItemType.isVisible = false
        }

        //ivItemThumbnail.transitionName = item.uid
        ivItemThumbnail.contentDescription = item.title

        thumbnailTarget = requestManager
            .load(item.thumbnailUrl)
            //.listener(createRequestListener(position))
            .centerCrop()
            .placeholder(R.color.empty_image)
            .into(ivItemThumbnail)
    }

    override fun unbind() {
        thumbnailTarget?.let {
            thumbnailTarget = null
            requestManager.clear(it)
        }
    }
}