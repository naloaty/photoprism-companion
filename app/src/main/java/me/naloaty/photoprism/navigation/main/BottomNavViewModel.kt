package me.naloaty.photoprism.navigation.main

import androidx.annotation.AnimRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.naloaty.photoprism.R
import me.naloaty.photoprism.navigation.main.model.ViewState

class BottomNavViewModel: ViewModel()  {
    private val _bottomNavigationState = MutableStateFlow<ViewState>(ViewState.Shown(null))
    val bottomNavigationState = _bottomNavigationState.asStateFlow()

    private var onAlbumsContent = false
    private var listScrolledByUser = false
    private var searchViewIsVisible = false


    fun onAlbumsNavigated() {
        onAlbumsContent = false
        showBottomNavigation(R.anim.zoom_out_enter_bottom)
    }

    fun onAlbumContentNavigated() {
        onAlbumsContent = true
        hideBottomNavigation(R.anim.zoom_in_exit_bottom)
    }

    fun onSearchViewShowing() {
        searchViewIsVisible = true

        if (!onAlbumsContent) {
            hideBottomNavigation(R.anim.slide_out_bottom)
        }
    }

    fun onSearchViewHidden() {
        searchViewIsVisible = false

        if (!onAlbumsContent) {
            showBottomNavigation(R.anim.slide_in_bottom)
        }
    }

    fun onListStateChanged(scrolledByUser: Boolean) {
        listScrolledByUser = scrolledByUser
    }

    fun onScrollingUp() {
        if (onAlbumsContent) return
        if (!listScrolledByUser) return
        if (searchViewIsVisible) return
        showBottomNavigation(R.anim.slide_in_bottom)
    }

    fun onScrollingDown() {
        if (onAlbumsContent) return
        if (!listScrolledByUser) return
        if (searchViewIsVisible) return
        hideBottomNavigation(R.anim.slide_out_bottom)
    }

    private fun hideBottomNavigation(@AnimRes animResId: Int? = null) {
        if (_bottomNavigationState.value is ViewState.Hidden) return
        _bottomNavigationState.value = ViewState.Hidden(animResId)
    }

    private fun showBottomNavigation(@AnimRes animResId: Int? = null) {
        if (_bottomNavigationState.value is ViewState.Shown) return
        _bottomNavigationState.value = ViewState.Shown(animResId)
    }


}