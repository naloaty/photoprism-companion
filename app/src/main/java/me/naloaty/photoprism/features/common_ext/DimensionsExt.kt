package me.naloaty.photoprism.features.common_ext

import android.content.Context
import android.util.TypedValue

fun Context.dpToPx(dp: Int): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    resources.displayMetrics
)