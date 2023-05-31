package me.naloaty.photoprism.features.gallery.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

interface MediaRepository {

    suspend fun getSearchResultStream(query: GallerySearchQuery): Flow<PagingData<MediaItem>>

    suspend fun getSearchResultCount(query: GallerySearchQuery): Flow<Int>
}