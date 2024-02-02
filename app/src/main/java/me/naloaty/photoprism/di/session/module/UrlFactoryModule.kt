package me.naloaty.photoprism.di.session.module

import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import me.naloaty.photoprism.common.AlbumsPreviewUrlFactory
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.MediaDownloadUrlFactory
import me.naloaty.photoprism.common.MediaPreviewUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.di.session.qualifier.AlbumsUrlFactory
import me.naloaty.photoprism.di.session.qualifier.MediaUrlFactory

@Module
interface UrlFactoryModule {

    @[Binds MediaUrlFactory]
    fun bindMediaPreviewUrlFactory(mediaUrlFactory: MediaPreviewUrlFactory): PreviewUrlFactory

    @[Binds MediaUrlFactory]
    fun bindMediaDownloadUrlFactory(mediaUrlFactory: MediaDownloadUrlFactory): DownloadUrlFactory

    @[Binds AlbumsUrlFactory]
    fun bindAlbumsPreviewUrlFactory(albumsUrlFactory: AlbumsPreviewUrlFactory): PreviewUrlFactory
}