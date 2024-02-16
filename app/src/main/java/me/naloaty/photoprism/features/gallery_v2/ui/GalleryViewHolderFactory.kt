package me.naloaty.photoprism.features.gallery_v2.ui

import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.features.gallery_v2.ui.model.MediaItemUiViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory

class GalleryViewHolderFactory : CoroutinesHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.li_gallery_media_item -> MediaItemUiViewHolder(view, clicks)
            else -> null
        }
    }
}