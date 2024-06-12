package me.naloaty.photoprism.common.common_recycler.model

import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.ItemCommonErrorBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

data object CommonErrorItem : ViewTyped {
    override val uid: String = this::class.java.name
    override val viewType: Int = R.layout.item_common_error
}

class CommonErrorItemViewHolder(view: View, clicks: TiRecyclerClickListener) :
    BaseViewHolder<CommonErrorItem>(view) {

    private val binding = ItemCommonErrorBinding.bind(view)

    init {
        clicks.accept(binding.btnRetry, this)
    }
}