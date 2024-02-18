package me.naloaty.photoprism.features.common_ext

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchView
import me.naloaty.photoprism.navigation.main.BottomNavViewModel

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
            if (
                RecyclerView.SCROLL_STATE_DRAGGING == previousState &&
                RecyclerView.SCROLL_STATE_SETTLING == newState
            ) {
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