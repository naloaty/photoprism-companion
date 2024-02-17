package me.naloaty.photoprism.features.common_recycler.model

import me.naloaty.photoprism.R
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

data object CommonEmptyItem : ViewTyped {
    override val uid: String = this::class.java.name
    override val viewType: Int = R.layout.layout_common_empty
}