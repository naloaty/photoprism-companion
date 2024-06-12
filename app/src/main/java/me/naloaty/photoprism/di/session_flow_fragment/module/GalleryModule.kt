package me.naloaty.photoprism.di.session_flow_fragment.module

import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import me.naloaty.photoprism.di.session_flow_fragment.module.GalleryModule.Bindings
import me.naloaty.photoprism.features.gallery.data.MediaRepositoryImpl
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import me.naloaty.photoprism.features.media_viewer.ui.DefaultVideoPlayerFactory
import me.naloaty.photoprism.features.media_viewer.ui.VideoPlayerFactory

@Module(includes = [Bindings::class])
object GalleryModule {

    @Module
    interface Bindings {
        @Binds
        fun bindMediaRepository(repositoryImpl: MediaRepositoryImpl): MediaRepository

        @Binds
        fun bindVideoPlayerFactory(factory: DefaultVideoPlayerFactory): VideoPlayerFactory
    }
}