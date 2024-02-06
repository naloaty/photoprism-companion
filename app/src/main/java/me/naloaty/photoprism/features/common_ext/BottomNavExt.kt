package me.naloaty.photoprism.features.common_ext

import androidx.recyclerview.widget.RecyclerView
import me.naloaty.photoprism.navigation.main.BottomNavViewModel

fun RecyclerView.syncWithBottomNav(bottomNavViewModel: BottomNavViewModel) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) { // Scrolling down
                bottomNavViewModel.onScrollingDown()
            } else { // Scrolling up
                bottomNavViewModel.onScrollingUp()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (
                RecyclerView.SCROLL_STATE_DRAGGING == newState
            ) {
                bottomNavViewModel.onListStateChanged(true)
            } else {
                bottomNavViewModel.onListStateChanged(false)
            }
        }
    })
}