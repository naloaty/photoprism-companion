package me.naloaty.photoprism.features.gallery.domain.usecase

import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository
import javax.inject.Inject

@SessionFlowFragementScope
class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke(albumUid: String) =
        repository.getAlbumByUid(albumUid)
}