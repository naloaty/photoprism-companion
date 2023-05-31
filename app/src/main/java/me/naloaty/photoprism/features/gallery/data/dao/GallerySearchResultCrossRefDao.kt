package me.naloaty.photoprism.features.gallery.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchResultCrossRef
import java.time.Instant

@Dao
interface GallerySearchResultCrossRefDao {

    @Upsert
    suspend fun upsert(references: List<GallerySearchResultCrossRef>)

    @Query(
        """DELETE FROM gallery_search_result_cross_ref
           WHERE query_id = :queryId"""
    )
    suspend fun deleteByQueryId(queryId: Long)
}