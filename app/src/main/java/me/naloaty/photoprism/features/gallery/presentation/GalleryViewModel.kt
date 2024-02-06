package me.naloaty.photoprism.features.gallery.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.naloaty.photoprism.features.common_search.SearchQuery.Config
import me.naloaty.photoprism.features.common_search.SearchViewModel
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.usecase.GetAlbumUseCase
import me.naloaty.photoprism.features.gallery.domain.usecase.GetSearchResultUseCase
import me.naloaty.photoprism.features.gallery.presentation.mapper.toGalleryListItem
import me.naloaty.photoprism.features.gallery.presentation.model.GalleryListItem
import javax.inject.Inject


private const val FULL_GALLERY_QUERY = ""
private const val EMPTY_ALBUM_FILTER = ""

class GalleryViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val getAlbumUseCase: GetAlbumUseCase
) : SearchViewModel<GallerySearchQuery, GalleryListItem>(
    defaultSearchQuery = GallerySearchQuery(FULL_GALLERY_QUERY)
) {

    private var albumFilter = EMPTY_ALBUM_FILTER

    private val _albumTitle = MutableStateFlow<String?>(null)
    val albumTitle = _albumTitle.asStateFlow()

    fun setAlbumFilter(albumUid: String) {
        albumFilter = "album:$albumUid"

        viewModelScope.launch {
            val album = getAlbumUseCase(albumUid)
            _albumTitle.value = album.title
        }

        performQueryAction()
    }

    override fun constructSearchQuery(searchText: String, config: Config): GallerySearchQuery {
        return GallerySearchQuery(
            value = "$albumFilter $searchText".trim(),
            config = config
        )
    }

    override suspend fun getSearchResultStream(
        query: GallerySearchQuery
    ): Flow<PagingData<GalleryListItem>> {
        return getSearchResultUseCase(query)
            .map { pagingData ->
                pagingData.map { mediaItem ->
                    mediaItem.toGalleryListItem()
                }
            }
    }

}