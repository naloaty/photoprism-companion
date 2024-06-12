package me.naloaty.photoprism.features.gallery.ui

import android.view.View
import com.bumptech.glide.RequestManager
import me.naloaty.photoprism.R
import me.naloaty.photoprism.common.common_recycler.model.CommonEmptyItem
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItemViewHolder
import me.naloaty.photoprism.common.common_recycler.model.CommonLoadingItem
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageErrorItemViewHolder
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageLoadingItem
import me.naloaty.photoprism.features.gallery.ui.items.MediaGroupTitleItemViewHolder
import me.naloaty.photoprism.features.gallery.ui.items.MediaThumbnailItemViewHolder
import me.naloaty.photoprism.features.gallery.ui.items.PlaceholderItem
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory

class GalleryViewHolderFactory(
    private val requestManager: RequestManager
): CoroutinesHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_gallery_media_thumbnail -> MediaThumbnailItemViewHolder(view, clicks, requestManager)
            R.layout.item_common_error -> CommonErrorItemViewHolder(view, clicks)
            R.layout.item_common_loading -> BaseViewHolder<CommonLoadingItem>(view)
            R.layout.item_common_empty -> BaseViewHolder<CommonEmptyItem>(view)
            R.layout.item_common_next_page_loading -> BaseViewHolder<CommonNextPageLoadingItem>(view)
            R.layout.item_common_next_page_error -> CommonNextPageErrorItemViewHolder(view, clicks)
            R.layout.item_gallery_media_placeholder -> BaseViewHolder<PlaceholderItem>(view)
            R.layout.item_gallery_media_group_title -> MediaGroupTitleItemViewHolder(view)
            else -> null
        }
    }
}