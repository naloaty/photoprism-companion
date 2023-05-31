package me.naloaty.photoprism.features.albums.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LiAlbumBinding
import me.naloaty.photoprism.di.fragment.FragmentScope
import me.naloaty.photoprism.features.albums.domain.model.Album
import javax.inject.Inject

@FragmentScope
class AlbumsAdapter @Inject constructor() :
    PagingDataAdapter<Album, RecyclerView.ViewHolder>(
        AlbumDiffCallback()
    ) {

    var onItemClickListener: ((Album) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.li_album -> AlbumViewHolder(
                LiAlbumBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalStateException("Unknown view type '$viewType'")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumViewHolder -> {
                val item = getItem(position)
                holder.dispose()
                holder.bind(item)
                holder.bindClickListener { onItemClickListener?.invoke(it) }
            }

            else -> throw IllegalStateException("Unknown view holder type '${holder::class.simpleName}'")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.li_album
    }

}