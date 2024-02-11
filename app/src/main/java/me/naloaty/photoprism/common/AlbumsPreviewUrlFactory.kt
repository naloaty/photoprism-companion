package me.naloaty.photoprism.common

import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.ApiUrl
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import javax.inject.Inject

@SessionFlowFragementScope
class AlbumsPreviewUrlFactory @Inject constructor(
    @ApiUrl apiUrl: String,
    session: LibraryAccountSession
): PreviewUrlFactory {

    private val previewBaseUrl = "$apiUrl/v1/albums"
    private val previewToken = session.previewToken

    override fun getSmallThumbnailUrl(identifier: String) =
        "$previewBaseUrl/$identifier/t/$previewToken/tile_224"

    override fun getMediumThumbnailUrl(identifier: String) =
        "$previewBaseUrl/$identifier/t/$previewToken/tile_500"

    override fun getLargeThumbnailUrl(identifier: String) =
        "$previewBaseUrl/$identifier/t/$previewToken/tile_1280"
}