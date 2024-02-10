package me.naloaty.photoprism.features.albums.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.di.session.SessionScope
import me.naloaty.photoprism.di.session.qualifier.AlbumsUrlFactory
import me.naloaty.photoprism.features.albums.data.mapper.toAlbum
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository
import javax.inject.Inject

@SessionScope
class AlbumRepositoryImpl @Inject constructor(
    database: AppDatabase,
    private val mediatorFactory: AlbumRemoteMediator.Factory,
    @AlbumsUrlFactory private val previewUrlFactory: PreviewUrlFactory
) : AlbumRepository {

    private val albumDao = database.albumDao()
    private val searchQueryDao = database.albumSearchQueryDao()
    private val searchResultDao = database.albumSearchResultDao()

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getSearchResultStream(query: AlbumSearchQuery): Flow<PagingData<Album>> {
        val searchQuery = withContext(Dispatchers.IO) {
            searchQueryDao.findOrInsert(query)
        }

        val resultFlow = Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE * 2,
                prefetchDistance = NETWORK_PAGE_SIZE,
                jumpThreshold = NETWORK_PAGE_SIZE * 3,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                searchResultDao.getPagingSource(queryId = searchQuery.id)
            },
            remoteMediator = mediatorFactory.create(query)
        ).flow

        return resultFlow.map { pagingData ->
            pagingData.map { searchResult ->
                searchResult.album.toAlbum(
                    previewUrlFactory = previewUrlFactory
                )
            }
        }
    }

    override suspend fun getAlbumByUid(albumUid: String): Album {
        return withContext(Dispatchers.IO) {
            albumDao.getAlbumByUid(albumUid).toAlbum(
                previewUrlFactory = previewUrlFactory
            )
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}