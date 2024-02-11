package me.naloaty.photoprism.features.gallery.domain.usecase

import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import javax.inject.Inject

@SessionFlowFragementScope
class GetSearchResultCountUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(query: GallerySearchQuery) =
        repository.getSearchResultCount(query)
}