package me.naloaty.photoprism.features.albums.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchResultCrossRef
import java.time.Instant

@Dao
interface AlbumSearchResultCrossRefDao {

    @Upsert
    suspend fun upsert(references: List<AlbumSearchResultCrossRef>)

    @Query(
        """DELETE FROM album_search_result_cross_ref
           WHERE query_id = :queryId"""
    )
    suspend fun deleteByQueryId(queryId: Long)
}