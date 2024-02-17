package me.naloaty.photoprism.features.common_recycler.model

import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LayoutCommonErrorBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

data object CommonErrorItem : ViewTyped {
    override val uid: String = this::class.java.name
    override val viewType: Int = R.layout.layout_common_error
}

class CommonErrorItemViewHolder(view: View, clicks: TiRecyclerClickListener) :
    BaseViewHolder<CommonErrorItem>(view) {

    private val binding = LayoutCommonErrorBinding.bind(view)

    init {
        clicks.accept(binding.btnRetry, this)
    }
}