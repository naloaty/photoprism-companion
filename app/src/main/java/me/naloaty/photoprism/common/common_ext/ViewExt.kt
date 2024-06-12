package me.naloaty.photoprism.common.common_ext

import android.view.View

fun View.updatePadding(start: Int = -1, top: Int = -1, end: Int = -1, bottom: Int = -1) {
    setPadding(
        if (start == -1) paddingLeft else start,
        if (top == -1) paddingTop else top,
        if (end == -1) paddingLeft else end,
        if (bottom == -1) paddingBottom else bottom
    )
}