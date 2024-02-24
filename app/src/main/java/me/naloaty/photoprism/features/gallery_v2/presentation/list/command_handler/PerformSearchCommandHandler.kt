package me.naloaty.photoprism.features.gallery_v2.presentation.list.command_handler

import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.naloaty.photoprism.common.common_paging.paginatorByOffsetPaging3
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.repository.MediaRepository
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.LoadMore
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.Refresh
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.Restart
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandHandler
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandResult.PerformSearchError
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryEvent
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

    override fun handle(commands: Flow<GalleryCommand>): Flow<GalleryEvent> {
        return commands
            .filterIsInstance<PerformSearch>()
            .flatMapLatest { command ->
                paginatorByOffsetPaging3(
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

//    paginatorByOffset(
//    refreshEvents = commands.filterIsInstance<Refresh>(),
//    restartEvents = commands.filterIsInstance<Restart>(),
//    loadMoreEvents = commands.filterIsInstance<LoadMore>(),
//    nextPageFilterStrategy = distinctBy { it.uid },
//    getFromRemote = { offset ->
//        val response = mediaService.getMediaItems(100, offset, query = command.query)
//        val items = response.body() ?: emptyList()
//        items.map { it.toMediaItem(previewUrlFactory, downloadUrlFactory) }
//    },
//    mapError = ::PerformSearchError,
//    mapState = ::PerformSearchResult
//    ).debounce(150)
//}

}