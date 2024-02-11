package me.naloaty.photoprism.features.albums.domain.usecase

import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository
import javax.inject.Inject

@SessionFlowFragementScope
class GetSearchResultUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(query: AlbumSearchQuery) =
        repository.getSearchResultStream(query)

}