package me.naloaty.photoprism.features.gallery.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.features.gallery.data.compound.MediaItemDbCompound
import me.naloaty.photoprism.features.gallery.data.entity.MediaItemDbEntity

@Dao
abstract class MediaItemDao(
    database: AppDatabase
) {
    private val mediaFileDao = database.mediaFileDao()

    @Transaction
    @Query("SELECT * FROM media_item WHERE uid = :itemUid LIMIT 1")
    abstract suspend fun findCompoundByUid(itemUid: String): MediaItemDbCompound?

    @Transaction
    open suspend fun upsert(compounds: List<MediaItemDbCompound>) {
        compounds.forEach { compound ->
            upsert(compound)
        }
    }

    @Transaction
    open suspend fun upsert(compound: MediaItemDbCompound) {
        compound.files.forEach { file ->
            file.itemUid = compound.item.uid
        }

        upsert(compound.item)
        mediaFileDao.upsert(compound.files)
    }

    @Upsert
    abstract suspend fun upsert(item: MediaItemDbEntity)
}