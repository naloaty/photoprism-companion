package me.naloaty.photoprism.features.common_recycler

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import me.naloaty.photoprism.databinding.LiLoadStateBinding

class LoadStateViewHolder(
    private val binding: LiLoadStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        when (loadState) {
            is LoadState.Loading -> bindAsLoading()
            is LoadState.Error -> bindAsError()
            else -> { /* Do nothing */ }
        }
    }

    private fun bindAsLoading(): Unit = with(binding) {
        piLoading.isVisible = true
        btnRetry.isVisible = false
    }

    private fun bindAsError(): Unit = with(binding) {
        piLoading.isVisible = false
        btnRetry.isVisible = true
    }

}