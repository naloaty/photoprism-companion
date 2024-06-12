package me.naloaty.photoprism.di.session_flow_fragment.module

import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import me.naloaty.photoprism.common.AlbumsPreviewUrlFactory
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.DownloadUrlFactoryImpl
import me.naloaty.photoprism.common.MediaPreviewUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.common.VideoUrlFactory
import me.naloaty.photoprism.common.VideoUrlFactoryImpl
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.media_viewer.ui.MediaCodecVideoFormatSupport
import me.naloaty.photoprism.features.media_viewer.ui.VideoFormatSupportProvider

@Module(includes = [UrlFactoryModule.Bindings::class])
object UrlFactoryModule {

    @Module
    interface Bindings {

        @Binds
        fun bindMediaPreviewUrlFactory(inType: MediaPreviewUrlFactory): PreviewUrlFactory<MediaItem>

        @Binds
        fun bindDownloadUrlFactory(inType: DownloadUrlFactoryImpl): DownloadUrlFactory

        @Binds
        fun bindAlbumsPreviewUrlFactory(inType: AlbumsPreviewUrlFactory): PreviewUrlFactory<Album>

        @Binds
        fun bindVideoUrlFactory(inType: VideoUrlFactoryImpl): VideoUrlFactory

        @Binds
        fun bindVideoFormatSupport(inType: MediaCodecVideoFormatSupport): VideoFormatSupportProvider
    }

}