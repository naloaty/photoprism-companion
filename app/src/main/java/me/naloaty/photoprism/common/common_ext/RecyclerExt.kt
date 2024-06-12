package me.naloaty.photoprism.common.common_ext

import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ensureItemIsVisible(
    itemGlobalPosition: Int,
    topOffset: Int = 0,
    bottomOffset: Int = 0
) {
    val layoutManager = checkNotNull(layoutManager as? LinearLayoutManager)
    val adapter = checkNotNull(adapter)

    val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
    val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

    val recyclerHeight = measuredHeight - paddingTop - paddingBottom

    var topBoundary = topOffset
    var bottomBoundary = recyclerHeight - bottomOffset

    if (clipToPadding) {
        topBoundary += paddingTop
        bottomBoundary -= paddingBottom
    }

    if (itemGlobalPosition !in firstVisibleItemPosition .. lastVisibleItemPosition) {
        val offset = if (itemGlobalPosition <= firstVisibleItemPosition) {
            topBoundary + 1 // RecyclerView behaves weirdly when offset is 0
        } else {
            val viewType = adapter.getItemViewType(itemGlobalPosition)
            val view = children.first { layoutManager.getItemViewType(it) == viewType }
            val itemHeight = view.measuredHeight
            bottomBoundary - itemHeight
        }

        layoutManager.scrollToPositionWithOffset(itemGlobalPosition, offset - paddingTop)
        return
    }

    val itemView = layoutManager.findViewByPosition(itemGlobalPosition) ?: return
    bottomBoundary -= itemView.measuredHeight

    when {
        itemView.top < topBoundary -> {
            layoutManager.scrollToPositionWithOffset(itemGlobalPosition, topBoundary - paddingTop)
        }
        itemView.top > bottomBoundary -> {
            layoutManager.scrollToPositionWithOffset(itemGlobalPosition, bottomBoundary - paddingTop)
        }
    }
}