package me.naloaty.photoprism.features.common_search

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.features.common_recycler.LoadStateRenderer
import me.naloaty.photoprism.navigation.main.BottomNavViewModel

fun <Query : SearchQuery, Result : Any> CoroutineScope.initSearch(
    searchView: SearchView,
    searchBar: SearchBar,
    applyButton: View,
    resetButton: View,
    searchViewModel: SearchViewModel<Query, Result>,
    bottomNavViewModel: BottomNavViewModel,
    loadStateRenderer: LoadStateRenderer? = null
) {
    searchView.editText.addTextChangedListener {
        searchViewModel.onSearchTextChanged(it.toString())
    }

    searchView.addTransitionListener { _, previousState, newState ->
        if (
            SearchView.TransitionState.HIDING == previousState &&
            SearchView.TransitionState.HIDDEN == newState
        ) {
            searchViewModel.onSearchViewHidden()
            bottomNavViewModel.onSearchViewHidden()
        }

        if (SearchView.TransitionState.SHOWING == newState) {
            bottomNavViewModel.onSearchViewShowing()
        }

        if (
            SearchView.TransitionState.SHOWING == previousState &&
            SearchView.TransitionState.SHOWN == newState
        ) {
            loadStateRenderer?.reset()
        }
    }

    applyButton.setOnClickListener {
        searchBar.setText(searchView.text)
        searchViewModel.onApplySearch()
        searchView.hide()
    }

    resetButton.setOnClickListener {
        searchBar.clearText()
        searchView.clearText()
        searchViewModel.onResetSearch()
        searchView.hide()
    }

    launch {
        searchViewModel.applySearchButtonEnabled.collectLatest {
            applyButton.isEnabled = it
        }
    }
}