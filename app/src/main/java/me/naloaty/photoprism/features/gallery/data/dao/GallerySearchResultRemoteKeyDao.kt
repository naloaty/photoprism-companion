package me.naloaty.photoprism.features.gallery.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchResultRemoteKey

@Dao
interface GallerySearchResultRemoteKeyDao {

    @Upsert
    suspend fun upsert(remoteKey: GallerySearchResultRemoteKey)

    @Query("SELECT * FROM gallery_search_result_remote_key WHERE query_id = :queryId")
    suspend fun findByQueryId(queryId: Long): GallerySearchResultRemoteKey?

    @Query("DELETE FROM gallery_search_result_remote_key WHERE query_id = :queryId")
    suspend fun deleteByQueryId(queryId: Long)
}