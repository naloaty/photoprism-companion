package me.naloaty.photoprism.features.gallery.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import me.naloaty.photoprism.features.gallery.data.entity.MediaFileDbEntity

@Dao
interface MediaFileDao {

    @Upsert
    suspend fun upsert(files: List<MediaFileDbEntity>)
}