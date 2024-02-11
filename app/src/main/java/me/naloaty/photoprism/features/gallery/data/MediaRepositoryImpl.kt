package me.naloaty.photoprism.features.gallery.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.MediaUrlFactory
import me.naloaty.photoprism.features.gallery.data.mapper.toMediaItem
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import javax.inject.Inject

@SessionFlowFragementScope
class MediaRepositoryImpl @Inject constructor(
    database: AppDatabase,
    private val mediatorFactory: MediaRemoteMediator.Factory,
    @MediaUrlFactory private val previewUrlFactory: PreviewUrlFactory,
    @MediaUrlFactory private val downloadUrlFactory: DownloadUrlFactory
) : MediaRepository {

    private val searchQueryDao = database.gallerySearchQueryDao()
    private val searchResultDao = database.gallerySearchResultDao()
    private val mediaItemDao = database.mediaItemDao()

    override suspend fun getMediaItemById(mediaItemUid: String): MediaItem? {
        return mediaItemDao.findCompoundByUid(mediaItemUid)
            ?.toMediaItem(
                previewUrlFactory = previewUrlFactory,
                downloadUrlFactory = downloadUrlFactory
            )
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getSearchResultStream(query: GallerySearchQuery): Flow<PagingData<MediaItem>> {
        val optimizedQuery = query.getOptimizedCopy()
        val searchQuery = searchQueryDao.findOrInsert(optimizedQuery)

        val resultFlow = Pager(
            config = PagingConfig(
                pageSize = 50,
                initialLoadSize = 100,
                prefetchDistance = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                searchResultDao.getPagingSource(queryId = searchQuery.id)
            },
            remoteMediator = mediatorFactory.create(optimizedQuery)
        ).flow

        return resultFlow.map { pagingData ->
            pagingData.map { searchResult ->
                searchResult.compound.toMediaItem(
                    previewUrlFactory = previewUrlFactory,
                    downloadUrlFactory = downloadUrlFactory
                )
            }
        }
    }

    override suspend fun getSearchResultCount(query: GallerySearchQuery): Flow<Int> {
        val optimizedQuery = query.getOptimizedCopy()
        val searchQuery = searchQueryDao.findOrInsert(optimizedQuery)
        return searchResultDao.getItemCount(searchQuery.id)
    }
}