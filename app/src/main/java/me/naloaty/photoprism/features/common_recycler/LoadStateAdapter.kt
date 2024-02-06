package me.naloaty.photoprism.features.common_recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LiLoadStateBinding

class LoadStateAdapter : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LiLoadStateBinding.inflate(inflater, parent, false)

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    override fun getStateViewType(loadState: LoadState): Int {
        return R.layout.li_load_state
    }

}