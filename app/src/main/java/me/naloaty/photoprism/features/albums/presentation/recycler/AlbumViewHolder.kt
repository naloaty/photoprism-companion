package me.naloaty.photoprism.features.albums.presentation.recycler

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LiAlbumBinding
import me.naloaty.photoprism.features.albums.domain.model.Album

class AlbumViewHolder(
    private val binding: LiAlbumBinding
) : ViewHolder(binding.root) {

    private var thumbnailTarget: Target<*>? = null
    private var album: Album? = null

    private fun bindAsPlaceholder() = with(binding) {
        tvTitle.isVisible = false
        tvItemCount.isVisible = false
        ivThumbnail.setImageResource(R.color.transparent)
    }

    private fun bindAsAlbum(album: Album) = with(binding) {
        tvTitle.isVisible = true
        tvItemCount.isVisible = true

        tvTitle.text = album.title
        tvItemCount.text = album.itemCount.toString()
        ivThumbnail.contentDescription = album.title

        thumbnailTarget = Glide
            .with(root)
            .load(album.smallThumbnailUrl)
            .centerCrop()
            .placeholder(R.color.empty_image)
            .into(ivThumbnail)
    }

    fun bind(album: Album?) {
        this.album = album

        if (album == null)
            bindAsPlaceholder()
        else
            bindAsAlbum(album)
    }

    fun bindClickListener(listener: (Album) -> Unit) {
        binding.root.setOnClickListener {
            album?.let { listener.invoke(it) }
        }
        binding.cvThumbnail.setOnClickListener {
            album?.let { listener.invoke(it) }
        }
    }

    fun dispose(): Unit = with(binding) {
        thumbnailTarget?.let {
            Glide
                .with(root)
                .clear(thumbnailTarget)

            thumbnailTarget = null
        }
    }
}