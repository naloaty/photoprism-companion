package me.naloaty.photoprism.features.media_viewer.ui.model

import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import me.naloaty.photoprism.databinding.LayoutPicturePreviewBinding
import me.naloaty.photoprism.features.common_ext.dpToPx
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped


data class ImageItemUi(
    override val uid: String,
    val title: String,
    val previewUrl: String,
) : ViewTyped {
    override val viewType: Int = me.naloaty.photoprism.R.layout.layout_picture_preview
}


class ImageItemUiViewHolder(view: View) :
    BaseViewHolder<ImageItemUi>(view) {

    private val binding = LayoutPicturePreviewBinding.bind(view)
    private var thumbnailTarget: Target<*>? = null
    private val requestManager = Glide.with(view)

    private val fadeFactory = DrawableCrossFadeFactory.Builder()
        .setCrossFadeEnabled(true)
        .build()

    private val circularDrawable: CircularProgressDrawable
        get() = binding.root.run {
            CircularProgressDrawable(context)
                .apply {
                    //setColorSchemeColors()
                    centerRadius = context.dpToPx(16)
                    strokeWidth = context.dpToPx(4)
                    start()
                }
        }

    override fun bind(item: ImageItemUi) = with(binding) {
        //ivItemThumbnail.transitionName = item.uid
        ivItemThumbnail.contentDescription = item.title

        thumbnailTarget = requestManager.load(item.previewUrl).apply {}
            //.listener(createRequestListener(position))
            .transition(withCrossFade(fadeFactory))
            .fitCenter()
            .placeholder(circularDrawable)
            .into(ivItemThumbnail)
    }

    override fun unbind() {
        thumbnailTarget?.let {
            thumbnailTarget = null
            requestManager.clear(it)
        }
    }
}