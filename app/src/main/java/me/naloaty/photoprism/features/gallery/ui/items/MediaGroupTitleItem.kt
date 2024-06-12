package me.naloaty.photoprism.features.gallery.ui.items

import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.ItemGalleryMediaGroupTitleBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

data class MediaGroupTitleItem(
    val title: String
): ViewTyped {

    override val uid: String
        get() = title

    override val viewType: Int
        get() = R.layout.item_gallery_media_group_title
}


class MediaGroupTitleItemViewHolder(view: View) : BaseViewHolder<MediaGroupTitleItem>(view) {

    private val binding = ItemGalleryMediaGroupTitleBinding.bind(view)

    override fun bind(item: MediaGroupTitleItem) {
        binding.tvDate.text = item.title
    }
}