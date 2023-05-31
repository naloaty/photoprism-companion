package me.naloaty.photoprism.di.session.module

import dagger.Binds
import dagger.Module
import me.naloaty.photoprism.common.AlbumsPreviewUrlFactory
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.MediaDownloadUrlFactory
import me.naloaty.photoprism.common.MediaPreviewUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.di.session.SessionScope
import me.naloaty.photoprism.di.session.qualifier.AlbumsUrlFactory
import me.naloaty.photoprism.di.session.qualifier.MediaUrlFactory

@Module
interface UrlFactoryModule {

    @[SessionScope Binds MediaUrlFactory]
    fun bindMediaPreviewUrlFactory(mediaUrlFactory: MediaPreviewUrlFactory): PreviewUrlFactory

    @[SessionScope Binds MediaUrlFactory]
    fun bindMediaDownloadUrlFactory(mediaUrlFactory: MediaDownloadUrlFactory): DownloadUrlFactory

    @[SessionScope Binds AlbumsUrlFactory]
    fun bindAlbumsPreviewUrlFactory(albumsUrlFactory: AlbumsPreviewUrlFactory): PreviewUrlFactory
}