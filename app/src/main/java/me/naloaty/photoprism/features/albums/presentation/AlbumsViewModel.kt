package me.naloaty.photoprism.features.albums.presentation

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.naloaty.photoprism.common.common_search.SearchQuery.Config
import me.naloaty.photoprism.common.common_search.SearchViewModel
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.domain.usecase.GetSearchResultUseCase
import javax.inject.Inject

private const val ALL_ALBUMS_QUERY = ""

class AlbumsViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultUseCase
): SearchViewModel<AlbumSearchQuery, Album>(
    defaultSearchQuery = AlbumSearchQuery(ALL_ALBUMS_QUERY)
) {

    override fun constructSearchQuery(searchText: String, config: Config): AlbumSearchQuery {
        return AlbumSearchQuery(
            value = searchText,
            config = config
        )
    }

    override suspend fun getSearchResultStream(query: AlbumSearchQuery): Flow<PagingData<Album>> {
        return getSearchResultsUseCase(query)
    }

}