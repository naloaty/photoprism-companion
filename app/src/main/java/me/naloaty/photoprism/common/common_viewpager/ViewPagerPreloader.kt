package me.naloaty.photoprism.common.common_viewpager

import android.widget.AbsListView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.bumptech.glide.ListPreloader.PreloadSizeProvider
import com.bumptech.glide.RequestManager

const val UNKNOWN_SCROLL_STATE = Int.MIN_VALUE;


class ViewPagerPreloader<T>(
    itemCountProvider: () -> Int,
    requestManager: RequestManager,
    preloadModelProvider: PreloadModelProvider<T>,
    preloadDimensionProvider: PreloadSizeProvider<T>,
    maxPreload: Int
): ViewPager2.OnPageChangeCallback() {

    private val viewPagerScrollListener = ViewPagerToListViewScrollListener(
        ListPreloader<T>(
            requestManager,
            preloadModelProvider,
            preloadDimensionProvider,
            maxPreload
        ),
        itemCountProvider
    )

    override fun onPageScrollStateChanged(state: Int) {
        viewPagerScrollListener.onPageScrollStateChanged(state)
    }

    override fun onPageSelected(position: Int) {
        viewPagerScrollListener.onPageSelected(position)
    }
}

class ViewPagerToListViewScrollListener(
    private val scrollListener: AbsListView.OnScrollListener,
    private val itemCountProvider: () -> Int
): ViewPager2.OnPageChangeCallback() {

    override fun onPageScrollStateChanged(state: Int) {
        val listViewState = when (state) {
            ViewPager2.SCROLL_STATE_IDLE -> AbsListView.OnScrollListener.SCROLL_STATE_IDLE
            ViewPager2.SCROLL_STATE_DRAGGING -> AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
            ViewPager2.SCROLL_STATE_SETTLING -> AbsListView.OnScrollListener.SCROLL_STATE_FLING
            else -> UNKNOWN_SCROLL_STATE
        }

        scrollListener.onScrollStateChanged(null, listViewState)
    }

    override fun onPageSelected(position: Int) {
        scrollListener.onScroll(null, position, 1, itemCountProvider())
    }

}