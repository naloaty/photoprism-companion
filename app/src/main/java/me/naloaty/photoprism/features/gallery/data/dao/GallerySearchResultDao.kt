package me.naloaty.photoprism.features.gallery.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.features.gallery.data.compound.MediaItemDbCompound
import me.naloaty.photoprism.features.gallery.data.compound.GallerySearchResultDbCompound
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchResultCrossRef
import java.time.Instant

@Dao
abstract class GallerySearchResultDao(
    database: AppDatabase
) {
    private val mediaItemDao = database.mediaItemDao()
    private val searchResultCrossRefDao = database.gallerySearchResultCrossRefDao()

    @Transaction
    @Query(
        """SELECT * FROM gallery_search_result_cross_ref 
           WHERE query_id = :queryId ORDER BY page ASC, `index` ASC"""
    )
    abstract fun getPagingSource(queryId: Long): PagingSource<Int, GallerySearchResultDbCompound>

    @Query(
        """SELECT COUNT(*) FROM gallery_search_result_cross_ref
           WHERE query_id = :queryId"""
    )
    abstract fun getItemCount(queryId: Long): Flow<Int>

    @Transaction
    open suspend fun insertOrRefresh(
        queryId: Long,
        page: Int,
        lastPage: Boolean,
        compounds: List<MediaItemDbCompound>
    ) {
        val currentDate = Instant.now()
        mediaItemDao.upsert(compounds)

        searchResultCrossRefDao.upsert(
            references = compounds.mapIndexed { index, compound ->
                GallerySearchResultCrossRef(
                    queryId = queryId,
                    itemUid = compound.item.uid,
                    page = page,
                    index = index
                )
            }
        )
    }

    suspend fun deleteByQueryId(queryId: Long) {
        searchResultCrossRefDao.deleteByQueryId(queryId)
    }
}