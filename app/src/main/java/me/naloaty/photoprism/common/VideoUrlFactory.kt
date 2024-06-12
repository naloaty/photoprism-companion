package me.naloaty.photoprism.common

import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.media_viewer.ui.VideoFormatSupportProvider
import javax.inject.Inject

interface VideoUrlFactory {

    fun createStreamingUrl(item: MediaItem.Streamable): String
}

private const val DEFAULT_VIDEO_PREVIEW_FORMAT = "avc"


class VideoUrlFactoryImpl @Inject constructor(
    private val urlProvider: LibraryUrlProvider,
    private val sessionProvider: SessionProvider,
    private val videoFormatSupport: VideoFormatSupportProvider,
) : VideoUrlFactory {

    private val previewToken: String
        get() = sessionProvider.session.previewToken

    private val apiUrl: String
        get() = urlProvider.libraryApiUrl

    override fun createStreamingUrl(item: MediaItem.Streamable): String {
        val videoFile = item.files.run {
            find {
                it.codec == "avc1"
            } ?: find {
                it.fileType == "mp4"
            } ?: find {
                it.isVideo == true
            }
        } ?:
        // Valid case for live photos.
        return "$apiUrl/v1/videos/${item.hash}/$previewToken/$DEFAULT_VIDEO_PREVIEW_FORMAT"

        val videoCodec = videoFile.codec ?: ""

        val previewFormat = when {
            (videoCodec == "hvc1" || videoCodec == "hev1") && videoFormatSupport.canPlayHevc() -> "hevc"
            videoCodec == "vp8" && videoFormatSupport.canPlayVp8() -> "vp8"
            videoCodec == "vp9" && videoFormatSupport.canPlayVp9() -> "vp9"
            (videoCodec == "av01" || videoCodec == "av1c") && videoFormatSupport.canPlayAv1() -> "av01"

            // WebM and OGV seems not supported.
            else -> DEFAULT_VIDEO_PREVIEW_FORMAT
        }

        return "$apiUrl/v1/videos/${videoFile.hash}/$previewToken/$previewFormat"
    }
}