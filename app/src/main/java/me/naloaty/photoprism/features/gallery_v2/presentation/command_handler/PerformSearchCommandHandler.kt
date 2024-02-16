package me.naloaty.photoprism.features.gallery_v2.presentation.command_handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommandHandler
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryEvent
import javax.inject.Inject

class PerformSearchCommandHandler @Inject constructor() : GalleryCommandHandler {
    override fun handle(commands: Flow<GalleryCommand>): Flow<GalleryEvent> {
        return commands
            .filterIsInstance<PerformSearch>()
            .map {
                PerformSearchResult(emptyList())
            }
    }
}