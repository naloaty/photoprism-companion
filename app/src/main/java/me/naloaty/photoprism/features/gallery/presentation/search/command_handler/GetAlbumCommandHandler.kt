package me.naloaty.photoprism.features.gallery.presentation.search.command_handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchCommand
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchCommand.GetAlbum
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchCommandHandler
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchCommandResult.GetAlbumResult
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchEvent
import javax.inject.Inject

class GetAlbumCommandHandler @Inject constructor(
    private val repository: AlbumRepository
) : GallerySearchCommandHandler {

    override fun handle(commands: Flow<GallerySearchCommand>): Flow<GallerySearchEvent> {
        return commands
            .filterIsInstance<GetAlbum>()
            .mapLatest { command ->
                GetAlbumResult(repository.getAlbumByUid(command.albumUid))
            }
    }

}