package me.naloaty.photoprism.features.gallery_v2.ui.model

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LiGalleryMediaItemBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

data class MediaItemUi(
    override val uid: String,
    val title: String,
    val thumbnailUrl: String,
    @DrawableRes val typeIcon: Int? = null,
    override val viewType: Int = R.layout.li_gallery_media_item
) : ViewTyped


class MediaItemUiViewHolder(view: View, clicks: TiRecyclerClickListener) :
    BaseViewHolder<MediaItemUi>(view, clicks) {

    private val binding = LiGalleryMediaItemBinding.bind(view)
    private var thumbnailTarget: Target<*>? = null
    private val requestManager = Glide.with(view)

    override fun bind(item: MediaItemUi) = with(binding) {
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