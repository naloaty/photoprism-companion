package me.naloaty.photoprism.features.media_viewer.ui

/**
 * A cache for video players that allows fast players reuse and seamless video view recreation.
 */
interface VideoPlayerCache {
    /**
     * @return cached player or newly created instance in case of miss.
     *
     * @param key an object with consistent hash code
     */
    fun getPlayer(key: Any): VideoPlayer

    /**
     * Releases the cached player and removes it from the cache.
     *
     * @param key an object with consistent hash code
     */
    fun releasePlayer(key: Any)
}