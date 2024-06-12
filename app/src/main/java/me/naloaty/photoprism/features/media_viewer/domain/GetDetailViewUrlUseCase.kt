package me.naloaty.photoprism.features.media_viewer.domain

import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory.Size.FIT_2048
import me.naloaty.photoprism.common.PreviewUrlFactory.Size.FIT_720
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import javax.inject.Inject


class GetDetailViewUrlUseCase @Inject constructor(
    private val previewUrlFactory: PreviewUrlFactory<MediaItem>
) {

    fun regular(item: MediaItem): String {
        return previewUrlFactory.create(item, FIT_720)
    }

    fun highRes(item: MediaItem): String {
        return previewUrlFactory.create(item, FIT_2048)
    }

}