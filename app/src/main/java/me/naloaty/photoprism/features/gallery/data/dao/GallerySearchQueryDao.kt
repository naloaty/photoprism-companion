package me.naloaty.photoprism.features.gallery.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchQueryDbEntity
import me.naloaty.photoprism.features.gallery.data.mapper.toGallerySearchQueryDbEntity
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import java.time.Instant

@Dao
abstract class GallerySearchQueryDao {

    @Transaction
    open suspend fun findOrInsert(query: GallerySearchQuery): GallerySearchQueryDbEntity {
        val searchQuery = findByQueryValue(query.value) ?: query.toGallerySearchQueryDbEntity()
        searchQuery.accessedAt = Instant.now()

        if (searchQuery.id == GallerySearchQueryDbEntity.UNSET_ID) {
            searchQuery.id = insertOrReplace(searchQuery)
        } else {
            insertOrReplace(searchQuery)
        }

        return searchQuery
    }

    @Query("SELECT * FROM gallery_search_query WHERE value = :value LIMIT 1")
    abstract suspend fun findByQueryValue(value: String): GallerySearchQueryDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrReplace(searchQuery: GallerySearchQueryDbEntity): Long
}