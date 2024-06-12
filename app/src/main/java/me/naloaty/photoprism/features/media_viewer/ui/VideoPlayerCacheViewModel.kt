package me.naloaty.photoprism.features.media_viewer.ui

import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * A view model that implements activity-scoped [VideoPlayerCache] based on a map.
 * The players must be released manually by [releasePlayer]
 */
class VideoPlayerCacheViewModel @Inject constructor(
    private val videoPlayerFactory: VideoPlayerFactory,
) : ViewModel(), VideoPlayerCache {

    private val cache = mutableMapOf<Any, VideoPlayer>()

    override fun getPlayer(key: Any): VideoPlayer {
        return getCachedPlayer(key)
            ?: createAndCacheMissingPlayer(key)
    }

    private fun getCachedPlayer(key: Any): VideoPlayer? {
        return cache[key]
            ?.also { player ->
                Timber.d(buildString {
                    appendLine("cache_hit:")
                    appendLine("key=$key")
                    appendLine("player=$player")
                })
            }
    }

    private fun createAndCacheMissingPlayer(key: Any): VideoPlayer {
        return videoPlayerFactory.createVideoPlayer()
            .also { createdPlayer ->
                cache[key] = createdPlayer

                Timber.d(buildString {
                    appendLine("cached_created_player:")
                    appendLine("player=$createdPlayer")
                    appendLine("cacheSize=${cache.size}")
                })
            }
    }

    override fun releasePlayer(key: Any) {
        cache[key]?.also { player ->
            player.release()
            cache.remove(key)

            Timber.d(buildString {
                appendLine("released_player:")
                appendLine("key=$key")
                appendLine("player=$player")
                appendLine("cacheSize=${cache.size}")
            })
        }
    }

    override fun onCleared() {
        cache.values.forEach(VideoPlayer::release)
        cache.clear()
        Timber.d("cleared_video_player_cache")
    }
}