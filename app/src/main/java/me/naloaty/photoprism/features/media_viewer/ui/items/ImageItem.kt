package me.naloaty.photoprism.features.media_viewer.ui.items

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.PagePicturePreviewBinding
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped


data class ImageItem(
    override val uid: String,
    val title: String,
    val contentAspect: Float,
    val contentUrl: String,
    val highResUrl: String,
    val thumbnailUrl: String,
) : ViewTyped {
    override val viewType: Int = R.layout.page_picture_preview
}


@SuppressLint("ClickableViewAccessibility")
class ImageItemViewHolder(
    view: View,
    private val requestManager: RequestManager
) : BaseViewHolder<ImageItem>(view) {

    private val binding = PagePicturePreviewBinding.bind(view)
    private var contentTarget: Target<*>? = null
    private var highResTarget: Target<*>? = null

    private val crossFadeFactory = DrawableCrossFadeFactory.Builder()
        .setCrossFadeEnabled(false)
        .build()

    private val contentOptions = RequestOptions()
        .fitCenter()

    init {
        binding.ivItemThumbnail.apply {
            setOnTouchListener { view, event ->
                var result = true
                //can scroll horizontally checks if there's still a part of the image
                //that can be scrolled until you reach the edge
                if (
                    event.pointerCount >= 2 ||
                    view.canScrollHorizontally(1) &&
                    canScrollHorizontally(-1)
                ) {
                    //multi-touch event
                    result = when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            // Disallow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(true)
                            // Disable touch on view
                            false
                        }
                        MotionEvent.ACTION_UP -> {
                            // Allow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(false)
                            true
                        }
                        else -> true
                    }
                }
                result
            }
        }
    }

    override fun onViewDetachedFromWindow() {
        binding.ivItemThumbnail.resetZoom()
    }

    override fun bind(item: ImageItem) {
        //ivItemThumbnail.transitionName = item.uid
        binding.ivItemThumbnail.contentDescription = item.title

        contentTarget = requestManager.load(item.contentUrl)
            //.listener(createRequestListener(position))
            .transition(withCrossFade(crossFadeFactory))
            .thumbnail(
                requestManager.load(item.thumbnailUrl)
                    .transform(ChangeAspectRatioFitCenter(item.contentAspect))
            )
            .apply(contentOptions)
            .into(binding.ivItemThumbnail)
    }

    override fun unbind() {
        contentTarget?.let {
            contentTarget = null
            requestManager.clear(it)
        }

        highResTarget?.let {
            highResTarget = null
            requestManager.clear(it)
        }
    }
}