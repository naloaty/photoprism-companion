package me.naloaty.photoprism.features.gallery.domain.usecase

import me.naloaty.photoprism.di.session.SessionScope
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import javax.inject.Inject

@SessionScope
class GetSearchResultCountUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(query: GallerySearchQuery) =
        repository.getSearchResultCount(query)
}