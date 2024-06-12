package me.naloaty.photoprism.features.albums.domain.usecase

import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory.Size.TILE_224
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.albums.domain.model.Album
import javax.inject.Inject

class GetThumbnailUrlUseCase @Inject constructor(
    private val previewUrlFactory: PreviewUrlFactory<Album>
) {

    operator fun invoke(item: Album): String {
        return previewUrlFactory.create(item, TILE_224)
    }
}