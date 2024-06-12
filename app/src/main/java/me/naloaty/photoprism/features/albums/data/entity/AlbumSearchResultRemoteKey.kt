package me.naloaty.photoprism.features.albums.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_search_result_remote_key")
class AlbumSearchResultRemoteKey(
    @ColumnInfo(name = "query_id")
    @PrimaryKey
    val queryId: Long,
    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)