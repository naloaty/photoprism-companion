package me.naloaty.photoprism.features.albums.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchResultRemoteKey

@Dao
interface AlbumSearchResultRemoteKeyDao {

    @Upsert
    suspend fun upsert(remoteKey: AlbumSearchResultRemoteKey)

    @Query("SELECT * FROM album_search_result_remote_key WHERE query_id = :queryId")
    suspend fun findByQueryId(queryId: Long): AlbumSearchResultRemoteKey?

    @Query("DELETE FROM album_search_result_remote_key WHERE query_id = :queryId")
    suspend fun deleteByQueryId(queryId: Long)
}