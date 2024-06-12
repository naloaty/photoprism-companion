package me.naloaty.photoprism.features.albums.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import me.naloaty.photoprism.features.albums.data.entity.AlbumDbEntity

@Dao
interface AlbumDao {

    @Upsert
    suspend fun upsert(albums: List<AlbumDbEntity>)

    @Query("SELECT * FROM album WHERE uid = :albumUid")
    suspend fun getAlbumByUid(albumUid: String): AlbumDbEntity
}