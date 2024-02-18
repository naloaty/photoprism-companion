package me.naloaty.photoprism.features.gallery_v2.presentation.search

import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery_v2.di.AlbumUid
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUiEvent.OnApplySearch
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUiEvent.OnResetSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUiEvent.OnSearchTextChanged
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

typealias GallerySearchStore = Store<GallerySearchState, GallerySearchEvent, GallerySearchNews>
typealias GalleryCommandHandler = CommandsFlowHandler<GallerySearchCommand, GallerySearchEvent>

const val FULL_GALLERY_QUERY = ""

class GallerySearchUpdate @Inject constructor(
    @AlbumUid val albumUid: String
): DslUpdate<GallerySearchState, GallerySearchEvent, GallerySearchCommand, GallerySearchNews>() {

    override fun NextBuilder.update(event: GallerySearchEvent) = when(event) {
        OnApplySearch -> handleOnApplySearch()
        OnResetSearch -> handleOnResetSearch()
        is OnSearchTextChanged -> state { copy(queryText = event.text.trim()) }
    }

    private fun NextBuilder.handleOnApplySearch() {
        news(
            GallerySearchNews.HideSearchView,
            GallerySearchNews.PerformSearch(
                GallerySearchQuery(value = withAlbumFilter(state.queryText))
            )
        )
    }

    private fun NextBuilder.handleOnResetSearch() {
        state { copy(queryText = FULL_GALLERY_QUERY) }
        handleOnApplySearch()
    }

    private fun NextBuilder.withAlbumFilter(queryText: String): String {
        return if (albumUid.isBlank()) {
            queryText
        } else {
            "album:$albumUid $queryText"
        }
    }
}