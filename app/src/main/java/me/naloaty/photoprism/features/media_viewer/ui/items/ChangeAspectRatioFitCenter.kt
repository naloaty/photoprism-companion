package me.naloaty.photoprism.features.media_viewer.ui.items

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.abs

private const val PRECISION = 0.0001

class ChangeAspectRatioFitCenter(
    private val targetAspect: Float,
): BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val currentWidth = toTransform.width
        val currentHeight = toTransform.height
        val currentAspect = currentWidth / currentHeight

        if (abs(currentAspect - targetAspect) < PRECISION) {
            return toTransform
        }

        val scaledWidth: Int
        val scaledHeight: Int

        if (targetAspect > 1) {
            scaledWidth = (currentHeight * targetAspect).toInt()
            scaledHeight = currentHeight
        } else {
            scaledWidth = currentWidth
            scaledHeight = (currentWidth / targetAspect).toInt()
        }

        val paddingVertical = ((scaledHeight - currentHeight) / 2).toFloat()
        val paddingHorizontal = ((scaledWidth - currentWidth) / 2).toFloat()

        val outputBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, toTransform.config)
        Canvas(outputBitmap).run {
            drawColor(Color.BLACK)
            drawBitmap(toTransform, paddingHorizontal, paddingVertical, null)
        }

        return outputBitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("ChangeAspectRatioFitCenter($targetAspect)".toByteArray(CHARSET))
    }
}