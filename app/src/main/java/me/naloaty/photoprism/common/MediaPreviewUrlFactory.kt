package me.naloaty.photoprism.common

import me.naloaty.photoprism.di.session.SessionScope
import me.naloaty.photoprism.di.session.qualifier.ApiUrl
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import javax.inject.Inject

@SessionScope
class MediaPreviewUrlFactory @Inject constructor(
    @ApiUrl apiUrl: String,
    session: LibraryAccountSession
) : PreviewUrlFactory {

    private val previewBaseUrl = "$apiUrl/v1"
    private val previewToken = session.previewToken

    override fun getSmallThumbnailUrl(identifier: String) =
        "$previewBaseUrl/t/$identifier/$previewToken/tile_224"

    override fun getMediumThumbnailUrl(identifier: String) =
        "$previewBaseUrl/t/$identifier/$previewToken/tile_500"

    override fun getLargeThumbnailUrl(identifier: String) =
        "$previewBaseUrl/t/$identifier/$previewToken/fit_1280"
}