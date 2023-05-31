package me.naloaty.photoprism.features.gallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.usecase.GetAlbumUseCase
import me.naloaty.photoprism.features.gallery.domain.usecase.GetSearchResultCountUseCase
import me.naloaty.photoprism.features.gallery.domain.usecase.GetSearchResultUseCase
import me.naloaty.photoprism.features.gallery.presentation.mapper.toGalleryListItem
import me.naloaty.photoprism.features.gallery.presentation.model.GalleryListItem
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val getAlbumUseCase: GetAlbumUseCase,
    private val getSearchResultCountUseCase: GetSearchResultCountUseCase
): ViewModel() {

    enum class SearchAction {
        NONE,
        QUERY,
        RESET
    }

    companion object {
        const val FULL_GALLERY_QUERY = ""
        const val EMPTY_ALBUM_FILTER = ""
    }

    private val searchQueryFlow = MutableStateFlow(GallerySearchQuery(FULL_GALLERY_QUERY))

    val searchQueryResult: Flow<PagingData<GalleryListItem>>
        get() = searchQueryFlow.flatMapLatest { query ->
            getSearchResultUseCase(query).map { pagingData ->
                pagingData.map { mediaItem ->
                    mediaItem.toGalleryListItem() as GalleryListItem
                }
            }.cachedIn(viewModelScope)
        }

    val searchQueryResultCount: Flow<Int>
        get() = searchQueryFlow.flatMapLatest { query ->
            getSearchResultCountUseCase(query)
        }

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
                performSearchQuery(
                    config = GallerySearchQuery.Config(
                        refresh = true
                    )
                )
            }
            SearchAction.RESET -> {
                updateSearchText(FULL_GALLERY_QUERY)
                performSearchQuery(
                    config = GallerySearchQuery.Config(
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

    fun setAlbumFilter(albumUid: String) {
        albumFilter = "album:$albumUid"

        viewModelScope.launch {
            val album = getAlbumUseCase(albumUid)
            _albumTitle.value = album.title
        }

        performSearchQuery(
            config = GallerySearchQuery.Config(
                refresh = true
            )
        )
    }

    private fun updateSearchText(text: String) {
        searchText = text
    }

    private fun updateApplyButtonState() {
        _applySearchButtonEnabled.value = searchText.isNotBlank()
    }

    private fun performSearchQuery(config: GallerySearchQuery.Config) {
        searchQueryFlow.value = GallerySearchQuery(
            value = "$albumFilter $searchText",
            config = config
        )
    }

}