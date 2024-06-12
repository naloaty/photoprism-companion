package me.naloaty.photoprism.common.common_recycler.model

import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.ItemCommonNextPageErrorBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

data object CommonNextPageErrorItem : ViewTyped {
    override val uid: String = this::class.java.name
    override val viewType: Int = R.layout.item_common_next_page_error
}

class CommonNextPageErrorItemViewHolder(view: View, clicks: TiRecyclerClickListener) :
    BaseViewHolder<CommonNextPageErrorItem>(view) {

    private val binding = ItemCommonNextPageErrorBinding.bind(view)

    init {
        clicks.accept(binding.btnRetry, this)
    }
}