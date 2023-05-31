package me.naloaty.photoprism.features.auth.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LibraryAccount(
    val libraryRoot: String,
    val username: String
): Parcelable {
    companion object {
        const val ACCOUNT_TYPE = "me.naloaty.photoprism"

        const val DATA_LIBRARY_ROOT = "library_root"
        const val DATA_USERNAME = "username"
    }
}