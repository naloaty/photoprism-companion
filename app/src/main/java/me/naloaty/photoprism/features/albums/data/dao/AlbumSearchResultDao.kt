package me.naloaty.photoprism.features.albums.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.features.albums.data.compound.AlbumSearchResultDbCompound
import me.naloaty.photoprism.features.albums.data.entity.AlbumDbEntity
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchResultCrossRef
import java.time.Instant

@Dao
abstract class AlbumSearchResultDao(
    database: AppDatabase
) {
    private val albumDao = database.albumDao()
    private val searchResultCrossRefDao = database.albumSearchResultCrossRefDao()

    @Transaction
    @Query(
        """SELECT * FROM album_search_result_cross_ref 
           WHERE query_id = :queryId ORDER BY page ASC, `index` ASC"""
    )
    abstract fun getPagingSource(queryId: Long): PagingSource<Int, AlbumSearchResultDbCompound>

    @Transaction
    open suspend fun insertOrRefresh(
        queryId: Long,
        page: Int,
        lastPage: Boolean,
        albums: List<AlbumDbEntity>
    ) {
        val currentDate = Instant.now()
        albumDao.upsert(albums)

        searchResultCrossRefDao.upsert(
            references = albums.mapIndexed { index, album ->
                AlbumSearchResultCrossRef(
                    queryId = queryId,
                    albumUid = album.uid,
                    page = page,
                    index = index,
                    updatedAt = currentDate
                )
            }
        )
    }

    suspend fun deleteByQueryId(queryId: Long) {
        searchResultCrossRefDao.deleteByQueryId(queryId)
    }
}