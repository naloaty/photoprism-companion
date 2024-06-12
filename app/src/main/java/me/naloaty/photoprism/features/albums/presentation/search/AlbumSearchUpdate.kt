package me.naloaty.photoprism.features.albums.presentation.search

import me.naloaty.photoprism.common.common_search.SearchQuery.Config
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchUiEvent.OnApplySearch
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchUiEvent.OnResetSearch
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchUiEvent.OnSearchTextChanged
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

typealias AlbumSearchStore = Store<AlbumSearchState, AlbumSearchEvent, AlbumSearchNews>

const val FULL_ALBUMS_QUERY = ""

class AlbumSearchUpdate @Inject constructor(
): DslUpdate<AlbumSearchState, AlbumSearchEvent, AlbumSearchCommand, AlbumSearchNews>() {

    override fun NextBuilder.update(event: AlbumSearchEvent) = when(event) {
        is OnApplySearch -> handleOnApplySearch()
        is OnResetSearch -> handleOnResetSearch()
        is OnSearchTextChanged -> state { copy(queryText = event.text.trim()) }
    }

    private fun NextBuilder.handleOnApplySearch() {
        news(AlbumSearchNews.HideSearchView)

        if (state.queryText != state.currentQuery?.value) {
            val query = AlbumSearchQuery(
                value = state.queryText,
                config = Config(refresh = true)
            )

            state { copy(currentQuery = query) }
            news(AlbumSearchNews.PerformSearch(query))
        }
    }

    private fun NextBuilder.handleOnResetSearch() {
        state { copy(queryText = FULL_ALBUMS_QUERY) }
        news(AlbumSearchNews.HideSearchView)

        if (state.queryText != state.currentQuery?.value) {
            val query = AlbumSearchQuery(
                value = state.queryText,
                config = Config(refresh = false)
            )

            state { copy(currentQuery = query) }
            news(AlbumSearchNews.PerformSearch(query))
        }
    }
}