package me.naloaty.photoprism.di.session.module

import dagger.Binds
import dagger.Module
import me.naloaty.photoprism.features.gallery.data.MediaRepositoryImpl
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository

@Module
interface GalleryModule {

    @Binds
    fun bindMediaRepository(repositoryImpl: MediaRepositoryImpl): MediaRepository
}