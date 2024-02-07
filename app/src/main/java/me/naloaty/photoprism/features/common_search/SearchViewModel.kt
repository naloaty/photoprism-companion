package me.naloaty.photoprism.features.common_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

abstract class SearchViewModel<Query : SearchQuery, Result : Any>(
    private val defaultSearchQuery: Query
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow<Query?>(defaultSearchQuery)

    val searchQueryResult: Flow<PagingData<Result>> =
        searchQueryFlow.flatMapLatest { query ->
            if (query == null) {
                flowOf(PagingData.empty())
            } else {
                getSearchResultStream(query).cachedIn(viewModelScope)
            }
        }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)


    private val _applySearchButtonEnabled = MutableStateFlow(false)
    val applySearchButtonEnabled = _applySearchButtonEnabled.asStateFlow()

    private var pendingAction = SearchAction.NONE
    private var searchText = defaultSearchQuery.value

    abstract suspend fun getSearchResultStream(query: Query): Flow<PagingData<Result>>

    abstract fun constructSearchQuery(searchText: String, config: SearchQuery.Config): Query

    fun onApplySearch() {
        pendingAction = SearchAction.QUERY
    }

    fun onResetSearch() {
        pendingAction = SearchAction.RESET
    }

    fun onSearchViewHidden() = when (pendingAction) {
        SearchAction.NONE -> Unit
        SearchAction.QUERY -> performQueryAction()
        SearchAction.RESET -> performResetAction()
    }.also { pendingAction = SearchAction.NONE }

    fun onSearchTextChanged(newText: String) {
        updateSearchText(newText)
        updateApplyButtonState()
    }

    protected fun performQueryAction() {
        val query = constructSearchQuery(
            searchText = searchText.trim(),
            config = SearchQuery.Config(refresh = true)
        )

        val currentQuery = searchQueryFlow.value

        if (currentQuery?.value != query.value) {
            updateSearchText(defaultSearchQuery.value)
            performSearchQuery(query)
        }

        performSearchQuery(query)
    }

    private fun performResetAction() {
        val currentQuery = searchQueryFlow.value

        val query = constructSearchQuery(
            searchText = searchText,
            config = SearchQuery.Config(refresh = false)
        )

        if (currentQuery?.value != query.value) {
            flushItems()
            updateSearchText(defaultSearchQuery.value)
            performSearchQuery(query)
        }
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

    private fun performSearchQuery(query: Query) {
        searchQueryFlow.value = query
    }

    private enum class SearchAction {
        NONE,
        QUERY,
        RESET
    }
}

interface SearchQuery {
    val value: String
    val config: Config
    data class Config(
        val refresh: Boolean = true
    )
}