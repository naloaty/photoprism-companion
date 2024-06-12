package me.naloaty.photoprism.common

import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import javax.inject.Inject

abstract class PreviewUrlFactory<T>(
    private val urlProvider: LibraryUrlProvider,
    private val sessionProvider: SessionProvider
) {

    protected val libraryApiUrl: String
        get() = urlProvider.libraryApiUrl

    protected val previewToken: String
        get() = sessionProvider.session.previewToken

    abstract fun create(item: T, size: Size): String

    protected fun Size.asEndpoint(): String {
        return this.name.lowercase()
    }

    enum class Size {
        TILE_100,
        TILE_224,
        TILE_500,
        FIT_720,
        FIT_1280,
        FIT_1920,
        FIT_2048,
        FIT_2560,
        FIT_3840,
        FIT_4096,
        FIT_7680
    }
}


class MediaPreviewUrlFactory @Inject constructor(
    urlProvider: LibraryUrlProvider,
    sessionProvider: SessionProvider,
) : PreviewUrlFactory<MediaItem>(urlProvider, sessionProvider) {

    override fun create(item: MediaItem, size: Size): String {
        return "$libraryApiUrl/v1/t/${item.hash}/$previewToken/${size.asEndpoint()}"
    }
}


class AlbumsPreviewUrlFactory @Inject constructor(
    urlProvider: LibraryUrlProvider,
    sessionProvider: SessionProvider,
): PreviewUrlFactory<Album>(urlProvider, sessionProvider) {

    override fun create(item: Album, size: Size): String {
        return "$libraryApiUrl/v1/albums/${item.uid}/t/$previewToken/${size.asEndpoint()}"
    }
}