package me.naloaty.photoprism.features.media_viewer.ui

import android.media.MediaFormat
import android.os.Build
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil
import javax.inject.Inject

class MediaCodecVideoFormatSupport @Inject constructor(): VideoFormatSupportProvider {
    override fun canPlayHevc() =
        isDecodingSupported(MediaFormat.MIMETYPE_VIDEO_HEVC)

    override fun canPlayVp8() =
        isDecodingSupported(MediaFormat.MIMETYPE_VIDEO_VP8)

    override fun canPlayVp9() =
        isDecodingSupported(MediaFormat.MIMETYPE_VIDEO_VP9)

    override fun canPlayAv1(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && isDecodingSupported(MediaFormat.MIMETYPE_VIDEO_AV1)

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun isDecodingSupported(mimeType: String) =
        runCatching { MediaCodecUtil.getDecoderInfo(mimeType, false, false) }.isSuccess
}