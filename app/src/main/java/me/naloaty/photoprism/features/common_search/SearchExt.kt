package me.naloaty.photoprism.features.common_search

import android.view.View
import android.view.inputmethod.EditorInfo
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
    
    searchView.editText.setOnEditorActionListener { _, actionId, _ ->
        if (EditorInfo.IME_ACTION_SEARCH == actionId) {
            searchViewModel.onApplySearch()
            true
        } else {
            false
        }
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

    applyButton.setOnClickListener { searchViewModel.onApplySearch() }
    resetButton.setOnClickListener { searchViewModel.onResetSearch() }

    launch {
        searchViewModel.searchState.collectLatest { state ->
            applyButton.isEnabled = state.applyButtonEnabled
        }
    }

    launch {
        searchViewModel.searchEffect.collectLatest { event ->
            when (event) {
                is SearchEffect.HideSearchView -> {
                    event.getContentIfNotHandled()?.let { searchView.hide() }
                }

                is SearchEffect.UpdateSearchText -> {
                    searchBar.setText(event.value)
                    searchView.setText(event.value)
                    searchView.editText.setSelection(event.value.length)
                }
            }
        }
    }
}