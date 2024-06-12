package me.naloaty.photoprism.features.media_viewer.ui.items

import android.view.View
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.PageVideoBinding
import me.naloaty.photoprism.features.media_viewer.ui.VideoPlayerCache
import me.naloaty.photoprism.features.media_viewer.ui.VideoPlayerViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

data class VideoItem(
    override val uid: String,
    val streamingUrl: String,
    val needsVideoControls: Boolean = true,
    val isLooped: Boolean = false,
) : ViewTyped {
    override val viewType: Int = R.layout.page_video
}

@OptIn(UnstableApi::class)
class VideoItemViewHolder(
    view: View,
    playerCache: VideoPlayerCache
) : BaseViewHolder<VideoItem>(view) {

    private val binding = PageVideoBinding.bind(view)

    private val playerViewHolder = VideoPlayerViewHolder(binding.videoView).apply {
        this.playerCache = playerCache
    }

    private var item: VideoItem? = null

    override fun onViewAttachedToWindow() {
        binding.videoView.player?.apply {
            if (isPlaying) {
                return
            }

            when (playbackState) {
                // When the player is stopped.
                Player.STATE_IDLE -> {
                    prepare()
                    playWhenReady = true
                }

                // When the player is loading.
                Player.STATE_BUFFERING -> {
                    playWhenReady = true
                }

                // When the player is ready.
                Player.STATE_READY -> {
                    play()
                }

                // When the video is ended.
                // Occurs when the screen is re-created,
                // as the player is stopped beforehand in other cases.
                Player.STATE_ENDED -> {
                    seekToDefaultPosition()
                    play()
                }
            }
        }
    }

    override fun onViewDetachedFromWindow() {
        binding.videoView.player?.apply {
            // Stop playback once the page is swiped.
            stop()

            // Seek to default position to start playback from the beginning
            // when swiping back to this page.
            // This seems the only right place to call this method.
            seekToDefaultPosition()
        }
    }

    override fun bind(item: VideoItem) {
        this.item = item
        val player = playerViewHolder.playerCache.getPlayer(key = item.uid)

        binding.videoView.player = player
        playerViewHolder.enableFatalPlaybackErrorListener(item)

        if (item.needsVideoControls) {
            binding.videoView.useController = true
            // If need to use the controller, show it manually.
            binding.videoView.showController()
        } else {
            // Setting to false hides the controller automatically.
            binding.videoView.useController = false
        }

        player.apply {
            // Only set up the player if its media item is changed.
            if (currentMediaItem?.mediaId != item.uid) {
                setMediaItem(
                    MediaItem.Builder()
                        .setMediaId(item.uid)
                        .setUri(item.streamingUrl)
                        .build()
                )

                volume = 1f

                repeatMode = if (item.isLooped) {
                    Player.REPEAT_MODE_ONE
                } else {
                    Player.REPEAT_MODE_OFF
                }

                // Start loading media on bind,
                // but prevent playback start until attached to the window.
                prepare()
                playWhenReady = false
            }
        }
    }

    override fun unbind() {
        binding.videoView.player = null
        item?.let { playerViewHolder.playerCache.releasePlayer(key = it.uid) }
    }
}