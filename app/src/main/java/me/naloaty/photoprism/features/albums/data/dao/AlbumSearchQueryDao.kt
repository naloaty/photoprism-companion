package me.naloaty.photoprism.features.albums.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchQueryDbEntity
import me.naloaty.photoprism.features.albums.data.mapper.toAlbumSearchQueryDbEntity
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import java.time.Instant

@Dao
abstract class AlbumSearchQueryDao {

    @Transaction
    open suspend fun findOrInsert(query: AlbumSearchQuery): AlbumSearchQueryDbEntity {
        val searchQuery = findByQueryValue(query.value) ?: query.toAlbumSearchQueryDbEntity()
        searchQuery.accessedAt = Instant.now()

        if (searchQuery.id == AlbumSearchQueryDbEntity.UNSET_ID) {
            searchQuery.id = insertOrReplace(searchQuery)
        } else {
            insertOrReplace(searchQuery)
        }

        return searchQuery
    }

    @Query("SELECT * FROM album_search_query WHERE value = :value LIMIT 1")
    abstract suspend fun findByQueryValue(value: String): AlbumSearchQueryDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrReplace(searchQuery: AlbumSearchQueryDbEntity): Long
}