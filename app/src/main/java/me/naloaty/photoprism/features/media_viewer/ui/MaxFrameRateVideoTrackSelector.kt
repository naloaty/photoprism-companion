package me.naloaty.photoprism.features.media_viewer.ui

import android.content.Context
import android.util.Pair
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.ExoTrackSelection

/**
 * A [DefaultTrackSelector] which selects the video track by the max known frame rate.
 * Therefore tracks wit unknown rate (-1) are not in favour.
 */
@OptIn(UnstableApi::class)
class MaxFrameRateVideoTrackSelector(
    context: Context,
) : DefaultTrackSelector(context) {

    override fun selectVideoTrack(
        mappedTrackInfo: MappedTrackInfo,
        rendererFormatSupports: Array<out Array<IntArray>>,
        mixedMimeTypeSupports: IntArray,
        params: Parameters
    ): Pair<ExoTrackSelection.Definition, Int>? {
        return (0 until mappedTrackInfo.rendererCount)
            .asSequence()
            .map { rendererIndex ->
                mappedTrackInfo.getTrackGroups(rendererIndex) to rendererIndex
            }
            .map { (trackGroups, rendererIndex) ->
                (0 until trackGroups.length)
                    .map { trackGroupIndex ->
                        trackGroups[trackGroupIndex] to rendererIndex
                    }
            }
            .flatten()
            .map { (trackGroup, rendererIndex) ->
                (0 until trackGroup.length)
                    .map { trackIndex ->
                        Triple(
                            trackGroup,
                            trackGroup.getFormat(trackIndex),
                            rendererIndex
                        )
                    }
            }
            .flatten()
            .maxByOrNull { (_, trackFormat) ->
                trackFormat.frameRate
            }
            ?.let { (trackGroup, trackFormat, rendererIndex) ->
                Pair.create(
                    ExoTrackSelection.Definition(
                        trackGroup,
                        trackGroup.indexOf(trackFormat)
                    ),
                    rendererIndex
                )
            }
    }
}
