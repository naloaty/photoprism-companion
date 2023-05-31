package me.naloaty.photoprism.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.naloaty.photoprism.features.albums.data.dao.AlbumDao
import me.naloaty.photoprism.features.albums.data.dao.AlbumSearchQueryDao
import me.naloaty.photoprism.features.albums.data.dao.AlbumSearchResultCrossRefDao
import me.naloaty.photoprism.features.albums.data.dao.AlbumSearchResultDao
import me.naloaty.photoprism.features.albums.data.dao.AlbumSearchResultRemoteKeyDao
import me.naloaty.photoprism.features.albums.data.entity.AlbumDbEntity
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchQueryDbEntity
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchResultCrossRef
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchResultRemoteKey
import me.naloaty.photoprism.features.gallery.data.dao.MediaFileDao
import me.naloaty.photoprism.features.gallery.data.dao.MediaItemDao
import me.naloaty.photoprism.features.gallery.data.dao.GallerySearchResultCrossRefDao
import me.naloaty.photoprism.features.gallery.data.dao.GallerySearchResultDao
import me.naloaty.photoprism.features.gallery.data.dao.GallerySearchQueryDao
import me.naloaty.photoprism.features.gallery.data.dao.GallerySearchResultRemoteKeyDao
import me.naloaty.photoprism.features.gallery.data.entity.MediaFileDbEntity
import me.naloaty.photoprism.features.gallery.data.entity.MediaItemDbEntity
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchResultCrossRef
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchQueryDbEntity
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchResultRemoteKey

@Database(
    version = 1,
    entities = [
        // Gallery feature
        MediaItemDbEntity::class,
        MediaFileDbEntity::class,
        GallerySearchQueryDbEntity::class,
        GallerySearchResultCrossRef::class,
        GallerySearchResultRemoteKey::class,

        // Album feature
        AlbumDbEntity::class,
        AlbumSearchQueryDbEntity::class,
        AlbumSearchResultCrossRef::class,
        AlbumSearchResultRemoteKey::class,
    ],
    exportSchema = false
)
@TypeConverters(
    value = [
        AppDatabaseConverters::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "photoprism.db"
    }

    // Gallery feature
    abstract fun mediaFileDao(): MediaFileDao
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun gallerySearchQueryDao(): GallerySearchQueryDao
    abstract fun gallerySearchResultDao(): GallerySearchResultDao
    abstract fun gallerySearchResultCrossRefDao(): GallerySearchResultCrossRefDao
    abstract fun gallerySearchResultRemoteKeyDao(): GallerySearchResultRemoteKeyDao

    // Albums feature
    abstract fun albumDao(): AlbumDao
    abstract fun albumSearchQueryDao(): AlbumSearchQueryDao
    abstract fun albumSearchResultDao(): AlbumSearchResultDao
    abstract fun albumSearchResultCrossRefDao(): AlbumSearchResultCrossRefDao
    abstract fun albumSearchResultRemoteKeyDao(): AlbumSearchResultRemoteKeyDao

}