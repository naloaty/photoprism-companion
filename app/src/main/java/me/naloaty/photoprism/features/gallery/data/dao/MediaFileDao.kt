package me.naloaty.photoprism.features.gallery.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import me.naloaty.photoprism.features.gallery.data.entity.MediaFileDbEntity

@Dao
interface MediaFileDao {

    @Upsert
    suspend fun upsert(files: List<MediaFileDbEntity>)
}