package me.naloaty.photoprism.features.albums.ui.model

import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.ItemAlbumBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

data class AlbumItemListUi(
    override val uid: String,
    val title: String,
    val itemCount: Int,
    val thumbnailUrl: String,
) : ViewTyped {
    override val viewType: Int = R.layout.item_album
}


class AlbumItemListUiViewHolder(view: View, clicks: TiRecyclerClickListener) :
    BaseViewHolder<AlbumItemListUi>(view) {

    private val binding = ItemAlbumBinding.bind(view)
    private var thumbnailTarget: Target<*>? = null
    private val requestManager = Glide.with(view)

    init {
        clicks.accept(binding.root, this)
        clicks.accept(binding.cvThumbnail, this)
    }

    override fun bind(item: AlbumItemListUi) = with(binding) {
        tvTitle.isVisible = true
        tvItemCount.isVisible = true

        tvTitle.text = item.title
        tvItemCount.text = item.itemCount.toString()
        ivThumbnail.contentDescription = item.title

        thumbnailTarget = Glide
            .with(root)
            .load(item.thumbnailUrl)
            .centerCrop()
            .placeholder(R.color.empty_image)
            .into(ivThumbnail)
    }

    override fun unbind() {
        thumbnailTarget?.let {
            thumbnailTarget = null
            requestManager.clear(it)
        }
    }
}