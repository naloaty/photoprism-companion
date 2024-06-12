package me.naloaty.photoprism.common.common_ext

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.search.SearchView
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import kotlin.math.abs

fun RecyclerView.syncWithBottomNav(bottomNavViewModel: BottomNavViewModel) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var previousState: Int = RecyclerView.SCROLL_STATE_IDLE

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) { // Scrolling down
                bottomNavViewModel.onScrollingDown()
            } else { // Scrolling up
                bottomNavViewModel.onScrollingUp()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val isDragging = RecyclerView.SCROLL_STATE_DRAGGING == newState
            val wasRecentlyDragging = RecyclerView.SCROLL_STATE_DRAGGING == previousState

            if (isDragging || wasRecentlyDragging) {
                bottomNavViewModel.onListStateChanged(true)
            } else {
                bottomNavViewModel.onListStateChanged(false)
            }

            previousState = newState
        }
    })
}

fun SearchView.syncWithBottomNav(bottomNavViewModel: BottomNavViewModel) {
    addTransitionListener { _, previousState, newState ->
        if (
            SearchView.TransitionState.HIDING == previousState &&
            SearchView.TransitionState.HIDDEN == newState
        ) {
            bottomNavViewModel.onSearchViewHidden()
        }

        if (SearchView.TransitionState.SHOWING == newState) {
            bottomNavViewModel.onSearchViewShowing()
        }
    }
}

private const val OFFSET_DELTA_THRESHOLD = 0.3

fun AppBarLayout.syncWithBottomNav(bottomNavViewModel: BottomNavViewModel) {
    addOnOffsetChangedListener(object : OnOffsetChangedListener {
        private var pivot: Float = 0f

        override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
            val appBarHeight = appBarLayout?.measuredHeight ?: return
            val visibleArea = 1 - abs(verticalOffset) / appBarHeight.toFloat()
            val delta = visibleArea - pivot

            if (abs(delta) > OFFSET_DELTA_THRESHOLD) {
                pivot = visibleArea

                when {
                    delta < 0 -> {
                        bottomNavViewModel.onScrollingDown()
                        bottomNavViewModel.onListStateChanged(true)
                    }

                    delta > 0 -> {
                        bottomNavViewModel.onScrollingUp()
                        bottomNavViewModel.onListStateChanged(true)
                    }
                }
            }
        }
    })
}