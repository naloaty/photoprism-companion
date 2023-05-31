package me.naloaty.photoprism.features.albums.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import me.naloaty.photoprism.api.endpoint.albums.service.PhotoPrismAlbumService
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.features.albums.data.compound.AlbumSearchResultDbCompound
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchResultRemoteKey
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.data.mapper.toAlbumDbEntities
import me.naloaty.photoprism.features.auth.domain.exception.AuthException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AlbumRemoteMediator @AssistedInject constructor(
    @Assisted("query") private val query: AlbumSearchQuery,
    private val database: AppDatabase,
    private val mediaService: PhotoPrismAlbumService,
) : RemoteMediator<Int, AlbumSearchResultDbCompound>() {

    private val searchQueryDao = database.albumSearchQueryDao()
    private val searchResultDao = database.albumSearchResultDao()
    private val searchResultRemoteKeyDao = database.albumSearchResultRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return if (query.config.refresh) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AlbumSearchResultDbCompound>
    ): MediatorResult {
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

            if (page == null)
                return MediatorResult.Success(endOfPaginationReached = true)

            val pageSize = state.config.pageSize
            val offset = page * pageSize

            val albums = mediaService.getAlbums(
                count = pageSize,
                offset = offset,
                query = query.value
            )

            val lastPage = albums.size < pageSize

            Timber.d(buildString {
                appendLine("pageSize=$pageSize")
                appendLine("offset=$offset")
                appendLine("responseSize=${albums.size}")
                appendLine("lastPage=$lastPage")
            })

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    searchResultDao.deleteByQueryId(queryId)
                    searchResultRemoteKeyDao.deleteByQueryId(queryId)
                    Timber.d("Search query result deleted")
                }

                searchResultRemoteKeyDao.upsert(
                    AlbumSearchResultRemoteKey(
                        queryId = queryId,
                        nextKey = if (lastPage) null else page + 1
                    )
                )

                searchResultDao.insertOrRefresh(
                    queryId = queryId,
                    page = page,
                    lastPage = lastPage,
                    albums = albums.toAlbumDbEntities()
                )
            }

            Timber.d("Search result updated")
            return MediatorResult.Success(endOfPaginationReached = lastPage)

        } catch (exception: IOException) {
            Timber.d(exception)
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            Timber.d(exception)
            return MediatorResult.Error(exception)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("query") query: AlbumSearchQuery
        ): AlbumRemoteMediator
    }

    companion object {
        private const val START_PAGE_INDEX = 0
    }
}