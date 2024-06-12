package me.naloaty.photoprism.features.gallery.presentation.list.command_handler

import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.naloaty.photoprism.common.common_paging.offsetPaginatorPaging3
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand.LoadMore
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand.Refresh
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand.Restart
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommandHandler
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommandResult.PerformSearchError
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryEvent
import javax.inject.Inject

class PerformSearchCommandHandler @Inject constructor(
    private val repository: MediaRepository
) : GalleryCommandHandler {

    private val diffCallback = object : DiffUtil.ItemCallback<MediaItem>() {

        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem == newItem
        }
    }

    @OptIn(FlowPreview::class)
    override fun handle(commands: Flow<GalleryCommand>): Flow<GalleryEvent> {
        return commands
            .filterIsInstance<PerformSearch>()
            .flatMapLatest { command ->
                offsetPaginatorPaging3(
                    refreshEvents = commands.filterIsInstance<Refresh>(),
                    restartEvents = commands.filterIsInstance<Restart>(),
                    loadMoreEvents = commands.filterIsInstance<LoadMore>().map { it.position },
                    pagingDataFlow = repository.getSearchResultStream(command.query),
                    diffCallback = diffCallback,
                    mapState = ::PerformSearchResult,
                    mapError = ::PerformSearchError
                ).debounce(timeoutMillis = 150)
            }
    }

}