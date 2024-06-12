package me.naloaty.photoprism.features.media_viewer.ui

fun interface VideoPlayerFactory {
    /**
     * Creates and initially sets up a video player.
     */
    fun createVideoPlayer(): VideoPlayer
}