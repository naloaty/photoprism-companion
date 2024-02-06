package me.naloaty.photoprism.features.gallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.usecase.GetAlbumUseCase
import me.naloaty.photoprism.features.gallery.domain.usecase.GetSearchResultUseCase
import me.naloaty.photoprism.features.gallery.presentation.mapper.toGalleryListItem
import javax.inject.Inject


private const val FULL_GALLERY_QUERY = ""
private const val EMPTY_ALBUM_FILTER = ""

class GalleryViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val getAlbumUseCase: GetAlbumUseCase
): ViewModel() {

    enum class SearchAction {
        NONE,
        QUERY,
        RESET
    }

    private val searchQueryFlow = MutableStateFlow<GallerySearchQuery?>(
        GallerySearchQuery(FULL_GALLERY_QUERY)
    )

    val searchQueryResult = searchQueryFlow.flatMapLatest { query ->
        if (query == null) {
            flowOf(PagingData.empty())
        } else {
            getSearchResultUseCase(query).map { pagingData ->
                pagingData.map { mediaItem -> mediaItem.toGalleryListItem() }
            }.cachedIn(viewModelScope)
        }
    }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    private val _applySearchButtonEnabled = MutableStateFlow(false)
    val applySearchButtonEnabled = _applySearchButtonEnabled.asStateFlow()

    private val _albumTitle = MutableStateFlow<String?>(null)
    val albumTitle = _albumTitle.asStateFlow()

    private var searchText = FULL_GALLERY_QUERY
    private var albumFilter = EMPTY_ALBUM_FILTER
    private var pendingAction = SearchAction.NONE


    fun onSearchViewHidden() {
        when(pendingAction) {
            SearchAction.NONE -> return
            SearchAction.QUERY -> {
                val query = constructSearchQuery(
                    config = GallerySearchQuery.Config(
                        refresh = true
                    )
                )

                performSearchQuery(query)
            }
            SearchAction.RESET -> {
                val currentQuery = searchQueryFlow.value

                val query = constructSearchQuery(
                    config = GallerySearchQuery.Config(
                        refresh = false
                    )
                )

                if (currentQuery?.value != query.value) {
                    flushItems()
                    updateSearchText(FULL_GALLERY_QUERY)
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

    fun setAlbumFilter(albumUid: String) {
        albumFilter = "album:$albumUid"

        viewModelScope.launch {
            val album = getAlbumUseCase(albumUid)
            _albumTitle.value = album.title
        }

        val query = constructSearchQuery(
            config = GallerySearchQuery.Config(
                refresh = true
            )
        )

        performSearchQuery(query)
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

    private fun constructSearchQuery(config: GallerySearchQuery.Config): GallerySearchQuery {
        return GallerySearchQuery(
            value = "$albumFilter $searchText",
            config = config
        )
    }

    private fun performSearchQuery(query: GallerySearchQuery) {
        searchQueryFlow.value = query
    }

}