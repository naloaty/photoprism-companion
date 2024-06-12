package me.naloaty.photoprism.common.common_recycler

import androidx.recyclerview.widget.RecyclerView

fun <T : RecyclerView.ViewHolder> RecyclerView.Adapter<T>.doOnPopulate(block: () -> Unit) {
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            block()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            block()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            block()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            block()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            block()
            unregisterAdapterDataObserver(this)
        }
    })
}