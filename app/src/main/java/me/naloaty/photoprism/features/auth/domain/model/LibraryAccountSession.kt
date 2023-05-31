package me.naloaty.photoprism.features.auth.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LibraryAccountSession(
    val sessionId: String,
    val previewToken: String,
    val downloadToken: String
): Parcelable {

    companion object {
        const val TOKEN_TYPE_PREVIEW = "preview_token"
        const val TOKEN_TYPE_SESSION = "session_id"
        const val TOKEN_TYPE_DOWNLOAD = "download_token"
    }
}