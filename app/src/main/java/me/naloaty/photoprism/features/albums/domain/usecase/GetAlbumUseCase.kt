package me.naloaty.photoprism.features.albums.domain.usecase

import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {

    suspend operator fun invoke(albumUid: String): Album {
        return repository.getAlbumByUid(albumUid)
    }
}