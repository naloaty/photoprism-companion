package me.naloaty.photoprism.features.gallery_v2.presentation.search

import me.naloaty.photoprism.common.common_search.SearchQuery.Config
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery_v2.di.AlbumUid
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUiEvent.OnApplySearch
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUiEvent.OnResetSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUiEvent.OnSearchTextChanged
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

typealias GallerySearchStore = Store<GallerySearchState, GallerySearchEvent, GallerySearchNews>

const val FULL_GALLERY_QUERY = ""

class GallerySearchUpdate @Inject constructor(
    @AlbumUid val albumUid: String
): DslUpdate<GallerySearchState, GallerySearchEvent, GallerySearchCommand, GallerySearchNews>() {

    override fun NextBuilder.update(event: GallerySearchEvent) = when(event) {
        is OnApplySearch -> handleOnApplySearch()
        is OnResetSearch -> handleOnResetSearch()
        is OnSearchTextChanged -> state { copy(queryText = event.text.trim()) }
    }

    private fun NextBuilder.handleOnApplySearch() {
        news(GallerySearchNews.HideSearchView)

        if (state.queryText != state.currentQuery?.value) {
            val query = GallerySearchQuery(
                value = state.queryText,
                albumUid = albumUid.ifBlank { null },
                config = Config(refresh = true)
            )

            state { copy(currentQuery = query) }
            news(GallerySearchNews.PerformSearch(query))
        }
    }

    private fun NextBuilder.handleOnResetSearch() {
        state { copy(queryText = FULL_GALLERY_QUERY) }
        news(GallerySearchNews.HideSearchView)

        if (state.queryText != state.currentQuery?.value) {
            val query = GallerySearchQuery(
                value = state.queryText,
                albumUid = albumUid.ifBlank { null },
                config = Config(refresh = false)
            )

            state { copy(currentQuery = query) }
            news(GallerySearchNews.PerformSearch(query))
        }
    }
}