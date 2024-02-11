package me.naloaty.photoprism.di.session_flow_fragment.module

import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import me.naloaty.photoprism.features.albums.data.AlbumRepositoryImpl
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository

@Module
interface AlbumsModule {

    @Binds
    fun bindAlbumRepository(repositoryImpl: AlbumRepositoryImpl): AlbumRepository
}