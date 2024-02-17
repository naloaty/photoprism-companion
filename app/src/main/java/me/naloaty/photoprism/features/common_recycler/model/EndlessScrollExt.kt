package me.naloaty.photoprism.features.common_recycler.model

import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import ru.tinkoff.mobile.tech.ti_recycler.scroll.DEFAULT_THRESHOLD

fun RecyclerView.endlessScrollFlow(
    threshHold: Int = DEFAULT_THRESHOLD
): Flow<Int> = callbackFlow {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }

    val listener = object : EndlessRecyclerOnScrollListener(threshHold) {
        override fun onLoadMore(currentPage: Int) {
            trySend(currentPage)
        }
    }

    addOnScrollListener(listener)

    awaitClose {
        listener.stopListen()
        removeOnScrollListener(listener)
    }
}
    .conflate()


// From FastAdapter
abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener {

    private var enabled = true
    private var previousTotal = 0
    private var isLoading = true
    private var visibleThreshold = RecyclerView.NO_POSITION
    var firstVisibleItem: Int = 0
        private set
    var visibleItemCount: Int = 0
        private set
    var totalItemCount: Int = 0
        private set

    private var isOrientationHelperVertical: Boolean = false
    private var orientationHelper: OrientationHelper? = null

    var currentPage = 0
        private set

    lateinit var layoutManager: RecyclerView.LayoutManager
        private set

    constructor()

    constructor(visibleThreshold: Int) {
        this.visibleThreshold = visibleThreshold
    }

    private fun findFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(0, layoutManager.childCount, false, true)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(child)
    }

    private fun findLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(recyclerView.childCount - 1, -1, false, true)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(child)
    }

    private fun findOneVisibleChild(fromIndex: Int, toIndex: Int, completelyVisible: Boolean,
                                    acceptPartiallyVisible: Boolean): View? {
        if (layoutManager.canScrollVertically() != isOrientationHelperVertical || orientationHelper == null) {
            isOrientationHelperVertical = layoutManager.canScrollVertically()
            orientationHelper = if (isOrientationHelperVertical)
                OrientationHelper.createVerticalHelper(layoutManager)
            else
                OrientationHelper.createHorizontalHelper(layoutManager)
        }

        val mOrientationHelper = this.orientationHelper ?: return null

        val start = mOrientationHelper.startAfterPadding
        val end = mOrientationHelper.endAfterPadding
        val next = if (toIndex > fromIndex) 1 else -1
        var partiallyVisible: View? = null
        var i = fromIndex
        while (i != toIndex) {
            val child = layoutManager.getChildAt(i)
            if (child != null) {
                val childStart = mOrientationHelper.getDecoratedStart(child)
                val childEnd = mOrientationHelper.getDecoratedEnd(child)
                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return child
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = child
                        }
                    } else {
                        return child
                    }
                }
            }
            i += next
        }
        return partiallyVisible
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (enabled) {
            if (!::layoutManager.isInitialized) {
                layoutManager = recyclerView.layoutManager
                    ?: throw RuntimeException("A LayoutManager is required")
            }

            if (visibleThreshold == RecyclerView.NO_POSITION) {
                visibleThreshold = findLastVisibleItemPosition(recyclerView) - findFirstVisibleItemPosition(recyclerView)
            }

            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = findFirstVisibleItemPosition(recyclerView)

            if (isLoading) {
                if (totalItemCount > previousTotal) {
                    isLoading = false
                    previousTotal = totalItemCount
                }
            }
            if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {

                currentPage++

                onLoadMore(currentPage)

                isLoading = true
            }
        }
    }

    fun stopListen() {
        enabled = false
    }

    @JvmOverloads
    fun resetPageCount(page: Int = 0) {
        previousTotal = 0
        isLoading = true
        currentPage = page
        onLoadMore(currentPage)
    }

    abstract fun onLoadMore(currentPage: Int)
}