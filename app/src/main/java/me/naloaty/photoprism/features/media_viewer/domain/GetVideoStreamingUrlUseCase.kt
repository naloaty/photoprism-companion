package me.naloaty.photoprism.features.media_viewer.domain

import me.naloaty.photoprism.common.VideoUrlFactory
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import javax.inject.Inject

class GetVideoStreamingUrlUseCase @Inject constructor(
    private val videoUrlFactory: VideoUrlFactory
) {

    operator fun invoke(item: MediaItem.Streamable): String {
        return videoUrlFactory.createStreamingUrl(item)
    }

}