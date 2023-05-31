package me.naloaty.photoprism.common.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.view.MotionEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.zhanghai.android.fastscroll.FastScroller
import me.zhanghai.android.fastscroll.PopupTextProvider
import me.zhanghai.android.fastscroll.Predicate


internal class PagingRecyclerViewHelper(
    private val view: RecyclerView,
    private val popupTextProvider: PopupTextProvider? = null
) : FastScroller.ViewHelper {

    private val tempRect = Rect()
    private var itemCount = 0


    override fun addOnPreDrawListener(onPreDraw: Runnable) {
        view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(
                canvas: Canvas, parent: RecyclerView,
                state: RecyclerView.State
            ) {
                onPreDraw.run()
            }
        })
    }

    override fun addOnScrollChangedListener(onScrollChanged: Runnable) {
        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                onScrollChanged.run()
            }
        })
    }

    override fun addOnTouchEventListener(onTouchEvent: Predicate<MotionEvent?>) {
        view.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(
                recyclerView: RecyclerView,
                event: MotionEvent
            ): Boolean {
                return onTouchEvent.test(event)
            }

            override fun onTouchEvent(
                recyclerView: RecyclerView,
                event: MotionEvent
            ) {
                onTouchEvent.test(event)
            }
        })
    }

    override fun getScrollRange(): Int {
        val itemCount = getItemCount()
        if (itemCount == 0) {
            return 0
        }
        val itemHeight = getItemHeight()
        if (itemHeight == 0) {
            return 0
        } else {
            return view.paddingTop + itemCount * itemHeight + view.paddingBottom
        }
    }

    override fun getScrollOffset(): Int {
        val firstItemPosition = getFirstItemPosition()
        if (firstItemPosition == RecyclerView.NO_POSITION) {
            return 0
        }
        val itemHeight = getItemHeight()
        val firstItemTop = getFirstItemOffset()
        return view.paddingTop + firstItemPosition * itemHeight - firstItemTop
    }

    override fun scrollTo(offset: Int) {
        // Stop any scroll in progress for RecyclerView.
        var offset = offset
        view.stopScroll()
        offset -= view.paddingTop
        val itemHeight = getItemHeight()
        // firstItemPosition should be non-negative even if paddingTop is greater than item height.
        val firstItemPosition = Math.max(0, offset / itemHeight)
        val firstItemTop = firstItemPosition * itemHeight - offset
        scrollToPositionWithOffset(firstItemPosition, firstItemTop)
    }

    override fun getPopupText(): CharSequence? {
        var popupTextProvider: PopupTextProvider? = popupTextProvider
        if (popupTextProvider == null) {
            val adapter = view.adapter
            if (adapter is PopupTextProvider) {
                popupTextProvider = adapter
            }
        }
        if (popupTextProvider == null) {
            return null
        }
        val position = getFirstItemAdapterPosition()

        if (position == RecyclerView.NO_POSITION) {
            return null
        } else {
            return popupTextProvider.getPopupText(position)
        }
    }

    fun updateItemCount(count: Int) {
        itemCount = count
    }

    private fun getItemCount(): Int {
        val linearLayoutManager = getVerticalLinearLayoutManager() ?: return 0

        if (linearLayoutManager is GridLayoutManager) {
            return (itemCount - 1) / linearLayoutManager.spanCount + 1
        } else {
            return itemCount
        }
    }


    private fun getItemHeight(): Int {
        if (view.childCount == 0) {
            return 0
        }
        val itemView = view.getChildAt(0)
        view.getDecoratedBoundsWithMargins(itemView, tempRect)
        return tempRect.height()
    }

    private fun getFirstItemPosition(): Int {
        var position = getFirstItemAdapterPosition()
        val linearLayoutManager = getVerticalLinearLayoutManager() ?: return RecyclerView.NO_POSITION
        if (linearLayoutManager is GridLayoutManager) {
            position /= linearLayoutManager.spanCount
        }
        return position
    }

    private fun getFirstItemAdapterPosition(): Int {
        if (view.childCount == 0) {
            return RecyclerView.NO_POSITION
        }
        val itemView = view.getChildAt(0)
        val linearLayoutManager = getVerticalLinearLayoutManager() ?: return RecyclerView.NO_POSITION
        return linearLayoutManager.getPosition(itemView)
    }

    private fun getFirstItemOffset(): Int {
        if (view.childCount == 0) {
            return RecyclerView.NO_POSITION
        }
        val itemView = view.getChildAt(0)
        view.getDecoratedBoundsWithMargins(itemView, tempRect)
        return tempRect.top
    }

    private fun scrollToPositionWithOffset(position: Int, offset: Int) {
        var position = position
        var offset = offset
        val linearLayoutManager = getVerticalLinearLayoutManager() ?: return
        if (linearLayoutManager is GridLayoutManager) {
            position *= linearLayoutManager.spanCount
        }
        // LinearLayoutManager actually takes offset from paddingTop instead of top of RecyclerView.
        offset -= view.paddingTop
        linearLayoutManager.scrollToPositionWithOffset(position, offset)
    }

    private fun getVerticalLinearLayoutManager(): LinearLayoutManager? {
        val layoutManager = view.layoutManager as? LinearLayoutManager ?: return null
        val linearLayoutManager = layoutManager

        if (linearLayoutManager.orientation != RecyclerView.VERTICAL) {
            return null
        } else {
            return linearLayoutManager
        }
    }
}