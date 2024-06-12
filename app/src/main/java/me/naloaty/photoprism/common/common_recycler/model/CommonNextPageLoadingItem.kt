package me.naloaty.photoprism.common.common_recycler.model

import me.naloaty.photoprism.R
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

data object CommonNextPageLoadingItem : ViewTyped {
    override val uid: String = this::class.java.name
    override val viewType: Int = R.layout.item_common_next_page_loading
}