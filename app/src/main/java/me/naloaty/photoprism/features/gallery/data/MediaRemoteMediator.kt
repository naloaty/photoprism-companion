package me.naloaty.photoprism.features.gallery.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yandex.yatagan.Assisted
import com.yandex.yatagan.AssistedFactory
import com.yandex.yatagan.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.naloaty.photoprism.api.endpoint.media.extension.fileCount
import me.naloaty.photoprism.api.endpoint.media.service.PhotoPrismMediaService
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.features.gallery.data.compound.GallerySearchResultDbCompound
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchResultRemoteKey
import me.naloaty.photoprism.features.gallery.data.mapper.toMediaItemDbCompounds
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MediaRemoteMediator @AssistedInject constructor(
    @Assisted("query") private val query: GallerySearchQuery,
    private val database: AppDatabase,
    private val mediaService: PhotoPrismMediaService,
) : RemoteMediator<Int, GallerySearchResultDbCompound>() {

    private val searchQueryDao = database.gallerySearchQueryDao()
    private val searchResultDao = database.gallerySearchResultDao()
    private val searchResultRemoteKeyDao = database.gallerySearchResultRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return if (query.config.refresh) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GallerySearchResultDbCompound>
    ): MediatorResult = withContext(Dispatchers.IO) {
        try {
            val queryId = searchQueryDao.findOrInsert(query).id

            val page = when(loadType) {
                LoadType.REFRESH -> {
                    START_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    null
                }

                LoadType.APPEND -> {
                    searchResultRemoteKeyDao.findByQueryId(queryId)?.nextKey
                }
            }

            Timber.d(buildString {
                appendLine("query=$query")
                appendLine("loadType=$loadType")
                appendLine("page=$page")
            })

            if (page == null) {
                return@withContext MediatorResult.Success(endOfPaginationReached = true)
            }

            val pageSize = state.config.pageSize
            val offset = page * pageSize

            val response = mediaService.getMediaItems(
                count = pageSize,
                offset = offset,
                query = query.run {
                    if (albumUid == null) {
                        value
                    } else {
                        "album:$albumUid $value"
                    }
                }
            )

            val mediaItems = response.body() ?: listOf()
            val fileCount = response.fileCount()
            val lastPage = fileCount < pageSize

            Timber.d(buildString {
                appendLine("pageSize=$pageSize")
                appendLine("offset=$offset")
                appendLine("fileCount=$fileCount")
                appendLine("responseSize=${mediaItems.size}")
                appendLine("lastPage=$lastPage")
            })

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    searchResultDao.deleteByQueryId(queryId)
                    searchResultRemoteKeyDao.deleteByQueryId(queryId)
                    Timber.d("Search query result deleted")
                }

                searchResultRemoteKeyDao.upsert(
                    GallerySearchResultRemoteKey(
                        queryId = queryId,
                        nextKey = if (lastPage) null else page + 1
                    )
                )

                searchResultDao.insertOrRefresh(
                    queryId = queryId,
                    page = page,
                    lastPage = lastPage,
                    compounds = mediaItems.toMediaItemDbCompounds()
                )
            }

            Timber.d("Search result updated")
            MediatorResult.Success(endOfPaginationReached = lastPage)
        } catch (exception: IOException) {
            Timber.d(exception)
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            Timber.d(exception, )
            MediatorResult.Error(exception)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("query") query: GallerySearchQuery
        ): MediaRemoteMediator
    }

    companion object {
        private const val START_PAGE_INDEX = 0
    }
}