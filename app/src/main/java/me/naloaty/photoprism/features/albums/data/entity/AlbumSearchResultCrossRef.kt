package me.naloaty.photoprism.features.albums.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.time.Instant

@Entity(
    tableName = "album_search_result_cross_ref",
    primaryKeys = ["query_id", "album_uid"],
    indices = [
        Index(
            name = "album_search_result_update_idx",
            value = ["query_id", "album_uid"]
        ),
        Index(
            name = "album_search_result_query_idx",
            value = ["query_id", "page", "index"]
        )
    ]
)
class AlbumSearchResultCrossRef(
    @ColumnInfo(name = "query_id")
    val queryId: Long,
    @ColumnInfo(name = "album_uid")
    val albumUid: String,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "index")
    val index: Int,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant
)