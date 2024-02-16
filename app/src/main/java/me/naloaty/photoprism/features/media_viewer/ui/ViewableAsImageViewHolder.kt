package me.naloaty.photoprism.features.media_viewer.ui

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.LayoutPicturePreviewBinding
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.ViewableAsImage

class ViewableAsImageViewHolder(
    private val binding: LayoutPicturePreviewBinding,
    private val onClickListener: (ViewableAsImage) -> Unit,
    private val onLoadCompletedListener: (position: Int) -> Unit
) : ViewHolder(binding.root) {

    private var thumbnailTarget: Target<*>? = null
    private var media: ViewableAsImage? = null

    private val requestManager = Glide.with(binding.ivItemThumbnail)

    init {
        binding.root.setOnClickListener {
            media?.let { onClickListener.invoke(it) }
        }
    }

    val sharedElement: View?
        get() = binding.ivItemThumbnail.takeIf { media != null }

    fun bind(position: Int, item: ViewableAsImage?) {
        dispose()
        this.media = item

        if (item == null) {
            bindAsPlaceholder()
        } else {
            bindAsRegularItem(position, item)
        }
    }

    private fun bindAsPlaceholder() = with(binding) {
        ivItemThumbnail.setImageResource(R.color.transparent)
    }

    private fun bindAsRegularItem(position: Int, item: ViewableAsImage) = with(binding) {
        thumbnailTarget = requestManager
            .load(item.largeThumbnailUrl)
            //.listener(createRequestListener(position))
            .fitCenter()
            .placeholder(R.color.empty_image)
            .into(ivItemThumbnail)
    }

    private fun createRequestListener(position: Int): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadCompletedListener(position)
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadCompletedListener(position)
                return true
            }
        }
    }

    private fun dispose() {
        thumbnailTarget?.let {
            thumbnailTarget = null
            requestManager.clear(it)
        }
    }
}