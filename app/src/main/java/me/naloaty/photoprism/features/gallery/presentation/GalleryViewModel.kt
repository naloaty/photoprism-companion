package me.naloaty.photoprism.features.gallery.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.naloaty.photoprism.features.common_search.SearchQuery.Config
import me.naloaty.photoprism.features.common_search.SearchViewModel
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.usecase.GetAlbumUseCase
import me.naloaty.photoprism.features.gallery.domain.usecase.GetSearchResultUseCase
import javax.inject.Inject


private const val FULL_GALLERY_QUERY = ""
private const val EMPTY_ALBUM_FILTER = ""

class GalleryViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val getAlbumUseCase: GetAlbumUseCase
) : SearchViewModel<GallerySearchQuery, MediaItem>(
    defaultSearchQuery = GallerySearchQuery(FULL_GALLERY_QUERY)
) {

    private var albumFilter = EMPTY_ALBUM_FILTER

    private val _albumTitle = MutableStateFlow<String?>(null)
    val albumTitle = _albumTitle.asStateFlow()

    var sharedElementPosition = 0

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
    ): Flow<PagingData<MediaItem>> {
        sharedElementPosition = 0
        return getSearchResultUseCase(query)
    }

}