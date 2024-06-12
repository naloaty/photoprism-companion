package me.naloaty.photoprism.features.albums.di

import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoList
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.features.albums.di.AlbumStoreModule.CommandHandlers
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommandHandler
import me.naloaty.photoprism.features.albums.presentation.list.AlbumState
import me.naloaty.photoprism.features.albums.presentation.list.AlbumStore
import me.naloaty.photoprism.features.albums.presentation.list.AlbumUpdate
import me.naloaty.photoprism.features.albums.presentation.list.command_handler.PerformSearchCommandHandler
import ru.tinkoff.kotea.core.KoteaStore

@Module(includes = [CommandHandlers::class])
object AlbumStoreModule {

    @Provides
    fun provideStore(
        update: AlbumUpdate,
        commandHandlers: List<AlbumCommandHandler>
    ): AlbumStore {
        return KoteaStore(
            initialState = AlbumState(),
            commandsFlowHandlers = commandHandlers,
            update = update
        )
    }

    @Module
    interface CommandHandlers {
        @[Binds IntoList]
        fun performSearch(inType: PerformSearchCommandHandler): AlbumCommandHandler
    }
}