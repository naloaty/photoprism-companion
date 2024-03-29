package me.naloaty.photoprism.common

import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.ApiUrl
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import javax.inject.Inject

@SessionFlowFragementScope
class MediaDownloadUrlFactory @Inject constructor(
    @ApiUrl apiUrl: String,
    session: LibraryAccountSession
) : DownloadUrlFactory {

    private val downloadBaseUrl = "$apiUrl/v1/dl"
    private val downloadToken = session.downloadToken

    override fun getDownloadUrl(identifier: String) =
        "$downloadBaseUrl/$identifier?t=$downloadToken"
}