package me.naloaty.photoprism.di.session.module

import dagger.Binds
import dagger.Module
import me.naloaty.photoprism.features.albums.data.AlbumRepositoryImpl
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository

@Module
interface AlbumsModule {

    @Binds
    fun bindAlbumRepository(repositoryImpl: AlbumRepositoryImpl): AlbumRepository
}