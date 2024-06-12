package me.naloaty.photoprism.features.gallery.ui.items

import me.naloaty.photoprism.R
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

object PlaceholderItem : ViewTyped {

    override val uid: String = this::class.java.name
    override val viewType: Int = R.layout.item_gallery_media_placeholder
}