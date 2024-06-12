package me.naloaty.photoprism.common

import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import javax.inject.Inject

interface DownloadUrlFactory {

    fun create(item: MediaItem): String
}


class DownloadUrlFactoryImpl @Inject constructor(
    private val urlProvider: LibraryUrlProvider,
    private val sessionProvider: SessionProvider
) : DownloadUrlFactory {

    private val downloadToken: String
        get() = sessionProvider.session.downloadToken

    override fun create(item: MediaItem): String {
        return "${urlProvider.libraryApiUrl}/v1/dl/${item.hash}?t=$downloadToken"
    }
}