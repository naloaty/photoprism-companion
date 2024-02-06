package me.naloaty.photoprism.features.albums.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.domain.usecase.GetSearchResultUseCase
import javax.inject.Inject

private const val ALL_ALBUMS_QUERY = ""

class AlbumsViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultUseCase
): ViewModel() {

    enum class SearchAction {
        NONE,
        QUERY,
        RESET
    }

    private val searchQueryFlow = MutableStateFlow<AlbumSearchQuery?>(
        AlbumSearchQuery(ALL_ALBUMS_QUERY)
    )

    val searchQueryResult = searchQueryFlow.flatMapLatest { query ->
        if (query == null) {
            flowOf(PagingData.empty())
        } else {
            getSearchResultsUseCase(query).cachedIn(viewModelScope)
        }
    }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)


    private val _applySearchButtonEnabled = MutableStateFlow(false)
    val applySearchButtonEnabled = _applySearchButtonEnabled.asStateFlow()

    private var searchText = ALL_ALBUMS_QUERY
    private var pendingAction = SearchAction.NONE


    fun onSearchViewHidden() {
        when(pendingAction) {
            SearchAction.NONE -> return
            SearchAction.QUERY -> {
                val query = constructSearchQuery(
                    config = AlbumSearchQuery.Config(
                        refresh = true
                    )
                )

                performSearchQuery(query)
            }
            SearchAction.RESET -> {
                val currentQuery = searchQueryFlow.value

                val query = constructSearchQuery(
                    config = AlbumSearchQuery.Config(
                        refresh = false
                    )
                )

                if (currentQuery?.value != query.value) {
                    flushItems()
                    updateSearchText(ALL_ALBUMS_QUERY)
                    performSearchQuery(query)
                }
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

    private fun flushItems() {
        searchQueryFlow.tryEmit(null)
    }

    private fun constructSearchQuery(config: AlbumSearchQuery.Config): AlbumSearchQuery {
        return AlbumSearchQuery(
            value = searchText,
            config = config
        )
    }

    private fun performSearchQuery(query: AlbumSearchQuery) {
        searchQueryFlow.value = query
    }

}