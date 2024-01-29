package me.naloaty.photoprism.features.albums.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.domain.usecase.GetSearchResultUseCase
import javax.inject.Inject


class AlbumsViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultUseCase
): ViewModel() {

    enum class SearchAction {
        NONE,
        QUERY,
        RESET
    }

    companion object {
        const val ALL_ALBUMS_QUERY = ""
    }

    private val searchQueryFlow = MutableStateFlow(AlbumSearchQuery(ALL_ALBUMS_QUERY))

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchQueryResult = searchQueryFlow.flatMapLatest { query ->
        getSearchResultsUseCase(query).cachedIn(viewModelScope)
    }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)


    private val _applySearchButtonEnabled = MutableStateFlow(false)
    val applySearchButtonEnabled = _applySearchButtonEnabled.asStateFlow()

    private var searchText = ALL_ALBUMS_QUERY
    private var pendingAction = SearchAction.NONE


    fun onSearchViewHidden() {
        when(pendingAction) {
            SearchAction.NONE -> return
            SearchAction.QUERY -> {
                performSearchQuery(
                    config = AlbumSearchQuery.Config(
                        refresh = true
                    )
                )
            }
            SearchAction.RESET -> {
                updateSearchText(ALL_ALBUMS_QUERY)
                performSearchQuery(
                    config = AlbumSearchQuery.Config(
                        refresh = false
                    )
                )
            }
        }
        pendingAction = SearchAction.NONE
    }

    fun onApplySearch() {
        pendingAction = SearchAction.QUERY
    }

    fun onResetSearch() {
        pendingAction = SearchAction.RESET
    }

    fun onSearchTextChanged(newText: String) {
        updateSearchText(newText)
        updateApplyButtonState()
    }

    private fun updateSearchText(text: String) {
        searchText = text
    }

    private fun updateApplyButtonState() {
        _applySearchButtonEnabled.value = searchText.isNotBlank()
    }

    private fun performSearchQuery(config: AlbumSearchQuery.Config) {
        searchQueryFlow.value = AlbumSearchQuery(
            value = searchText,
            config = config
        )
    }

}