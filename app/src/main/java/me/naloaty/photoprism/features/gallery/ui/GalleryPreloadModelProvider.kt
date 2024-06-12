package me.naloaty.photoprism.features.gallery.ui

import com.bumptech.glide.RequestManager
import me.naloaty.photoprism.R
import me.naloaty.photoprism.common.common_recycler.ViewTypedPreloadModelProvider
import me.naloaty.photoprism.features.media_viewer.ui.items.ImageItem
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter

class GalleryPreloadModelProvider(
    private val requestManager: RequestManager,
    adapter: BaseTiAdapter<*, *>
) : ViewTypedPreloadModelProvider(adapter) {

    override val factoriesRegistry = FactoriesRegistry {
        requestFactory<ImageItem> {
            requestManager
                .load(it.thumbnailUrl)
                .centerCrop()
                .placeholder(R.color.empty_image)
        }
    }
}