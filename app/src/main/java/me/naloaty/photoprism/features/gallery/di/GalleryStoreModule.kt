package me.naloaty.photoprism.features.gallery.di

import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoList
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.features.gallery.di.GalleryStoreModule.CommandHandlers
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommandHandler
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryState
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryStore
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUpdate
import me.naloaty.photoprism.features.gallery.presentation.list.command_handler.PerformSearchCommandHandler
import ru.tinkoff.kotea.core.KoteaStore

@Module(includes = [CommandHandlers::class])
object GalleryStoreModule {

    @Provides
    fun provideStore(
        update: GalleryUpdate,
        commandHandlers: List<GalleryCommandHandler>
    ): GalleryStore {
        return KoteaStore(
            initialState = GalleryState(),
            commandsFlowHandlers = commandHandlers,
            update = update
        )
    }

    @Module
    interface CommandHandlers {
        @[Binds IntoList]
        fun performSearch(inType: PerformSearchCommandHandler): GalleryCommandHandler
    }
}