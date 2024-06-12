package me.naloaty.photoprism.features.gallery.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import javax.inject.Inject


class GetSearchResultUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(query: GallerySearchQuery): Flow<PagingData<MediaItem>> {
        return repository.getSearchResultStream(query)
    }
}