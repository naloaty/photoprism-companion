package me.naloaty.photoprism.features.gallery.domain.usecase

import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory.Size.TILE_224
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import javax.inject.Inject


class GetThumbnailUrlUseCase @Inject constructor(
    private val previewUrlFactory: PreviewUrlFactory<MediaItem>
) {

    operator fun invoke(item: MediaItem): String {
        return previewUrlFactory.create(item, TILE_224)
    }
}