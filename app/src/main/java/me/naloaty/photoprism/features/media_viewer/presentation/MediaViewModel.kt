package me.naloaty.photoprism.features.media_viewer.presentation

import androidx.lifecycle.ViewModel
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class MediaViewModel @Inject constructor(
    private val repository: MediaRepository
): ViewModel() {

    suspend fun getPreviewUrl(mediaItemUid: String): String? {
        return repository.getMediaItemById(mediaItemUid)?.mediumThumbnailUrl
    }

}