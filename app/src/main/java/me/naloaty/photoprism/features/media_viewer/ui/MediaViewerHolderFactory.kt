package me.naloaty.photoprism.features.media_viewer.ui

import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.common.common_recycler.model.CommonEmptyItem
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItemViewHolder
import me.naloaty.photoprism.common.common_recycler.model.CommonLoadingItem
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageErrorItemViewHolder
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageLoadingItem
import me.naloaty.photoprism.features.media_viewer.ui.model.ImageItemUiViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory

class MediaViewerHolderFactory : CoroutinesHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.layout_picture_preview -> ImageItemUiViewHolder(view)
            R.layout.layout_common_error -> CommonErrorItemViewHolder(view, clicks)
            R.layout.layout_common_loading -> BaseViewHolder<CommonLoadingItem>(view)
            R.layout.layout_common_empty -> BaseViewHolder<CommonEmptyItem>(view)
            R.layout.layout_common_next_page_loading ->
                BaseViewHolder<CommonNextPageLoadingItem>(view)
            R.layout.layout_common_next_page_error ->
                CommonNextPageErrorItemViewHolder(view, clicks)
            else -> null
        }
    }

}