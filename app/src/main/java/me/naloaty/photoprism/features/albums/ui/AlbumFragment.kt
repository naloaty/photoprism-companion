package me.naloaty.photoprism.features.albums.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.doOnLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.sessionFlowFragmentViewModel
import me.naloaty.photoprism.base.storeViaViewModel
import me.naloaty.photoprism.common.common_ext.addOnBackPressedCallback
import me.naloaty.photoprism.common.common_ext.syncWithBottomNav
import me.naloaty.photoprism.common.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageErrorItem
import me.naloaty.photoprism.common.common_recycler.pagingEndlessScrollFlow
import me.naloaty.photoprism.databinding.FragmentAlbumsBinding
import me.naloaty.photoprism.di.injector.sessionComponentOwner
import me.naloaty.photoprism.features.albums.presentation.list.AlbumNews
import me.naloaty.photoprism.features.albums.presentation.list.AlbumUiEvent
import me.naloaty.photoprism.features.albums.presentation.list.AlbumUiEvent.OnPerformSearch
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchNews
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchNews.HideSearchView
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchNews.PerformSearch
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchUiEvent
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchUiEvent.OnResetSearch
import me.naloaty.photoprism.features.albums.ui.model.AlbumSearchUiState
import me.naloaty.photoprism.features.albums.ui.model.AlbumUiState
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import me.naloaty.photoprism.navigation.navigateSafely
import me.naloaty.photoprism.util.EMPTY_STRING
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.diff.ViewTypedDiffCallback
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.TiRecyclerCoroutines
import timber.log.Timber


private const val MIN_ALBUM_ITEMS_PER_ROW = 1
private const val ALBUM_CELL_SPAN = 1

class AlbumFragment : BaseSessionFragment(R.layout.fragment_albums) {

    private val binding: FragmentAlbumsBinding by viewBinding()

    private val component by sessionComponentOwner {
        albumComponentFactory().create()
    }

    private val store by storeViaViewModel { component.albumStore }
    private val searchStore by storeViaViewModel { component.albumSearchStore }

    private val bottomNavViewModel: BottomNavViewModel by sessionFlowFragmentViewModel()

    private var adapter: RecyclerView.Adapter<*> by viewLifecycleProperty()
    private var tiRecycler: TiRecyclerCoroutines<ViewTyped> by viewLifecycleProperty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBackNavigation()

        store.collectOnCreate(
            fragment = this,
            uiStateMapper = component.uiStateMapper,
            stateCollector = ::collectState,
            newsCollector = ::collectNews
        )
        searchStore.collectOnCreate(
            fragment = this,
            uiStateMapper = component.searchUiStateMapper,
            stateCollector = ::collectSearchState,
            newsCollector = ::collectSearchNews
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initSearch()
    }

    private fun setupBackNavigation() {
        addOnBackPressedCallback {
            when {
                binding.searchView.isShowing -> binding.searchView.hide()
                binding.searchView.text.isNotEmpty() -> searchStore.dispatch(OnResetSearch)
                else -> findNavController().navigateUp()
            }
        }
    }

    private fun initRecyclerView() {
        val gridLayoutManager = createGridLayoutManager()

        binding.root.doOnLayout {
            gridLayoutManager.spanCount = calculateAlbumsPerRow()
        }

        tiRecycler = TiRecyclerCoroutines(
            binding.rvAlbums, AlbumViewHolderFactory(), ViewTypedDiffCallback()
        ) {
            layoutManager = gridLayoutManager
        }

        adapter = tiRecycler.adapter

        viewLifecycleOwner.lifecycleScope.launch {
            merge(
                tiRecycler.adapter.holderFactory.clickPosition(R.layout.item_album)
                    .map { AlbumUiEvent.OnClickAlbumItem(tiRecycler.adapter.items[it].uid) },
                tiRecycler.clickedItem<CommonErrorItem>(R.layout.item_common_error)
                    .map { AlbumUiEvent.OnClickRestart },
                tiRecycler.clickedItem<CommonNextPageErrorItem>(R.layout.item_common_next_page_error)
                    .map { AlbumUiEvent.OnClickRestart },
                tiRecycler.recyclerView.pagingEndlessScrollFlow()
                    .map { AlbumUiEvent.OnLoadMore(it) },
            ).onEach {
                Timber.d("UiEvent: $it")
            }.collect(store::dispatch)
        }

        //tiRecycler.recyclerView.syncWithBottomNav(bottomNavViewModel)
    }

    private fun initSearch() = with(binding) {
        searchView.editText.addTextChangedListener {
            searchStore.dispatch(
                AlbumSearchUiEvent.OnSearchTextChanged(
                    it?.toString() ?: EMPTY_STRING
                )
            )
        }

        searchView.editText.setOnEditorActionListener { _, actionId, _ ->
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                searchStore.dispatch(AlbumSearchUiEvent.OnApplySearch)
                true
            } else {
                false
            }
        }

        searchViewContent.btnApply.setOnClickListener {
            searchStore.dispatch(AlbumSearchUiEvent.OnApplySearch)
        }

        searchViewContent.btnReset.setOnClickListener {
            searchStore.dispatch(AlbumSearchUiEvent.OnResetSearch)
        }

        searchView.syncWithBottomNav(bottomNavViewModel)
        appBarLayout.syncWithBottomNav(bottomNavViewModel)
    }

    private fun collectState(state: AlbumUiState) {
        tiRecycler.setItems(state.listItems)
    }

    private fun collectNews(news: AlbumNews) = when (news) {
        is AlbumNews.OpenGalleryView -> {
            val directions = AlbumFragmentDirections.actionViewAlbumContent(news.albumUid)
            findNavController().navigateSafely(directions)
        }
    }

    private fun collectSearchState(state: AlbumSearchUiState) = with(binding) {
        searchViewContent.btnApply.isEnabled = state.applyBtnEnabled

        if (searchBar.hint != state.searchBarHint) {
            searchBar.hint = state.searchBarHint
        }

        if (searchView.hint != state.searchBarHint) {
            searchView.hint = state.searchBarHint
        }

        if (searchBar.text != state.searchBarText) {
            searchBar.setText(state.searchBarText)
        }

        if (searchView.text.toString() != state.searchViewText) {
            searchView.setText(state.searchViewText)
            searchView.editText.setSelection(state.searchViewText.length)
        }
    }

    private fun collectSearchNews(news: AlbumSearchNews) = when(news) {
        is HideSearchView -> binding.searchView.hide()
        is PerformSearch -> store.dispatch(OnPerformSearch(news.query))
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        return GridLayoutManager(context, MIN_ALBUM_ITEMS_PER_ROW).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (adapter.getItemViewType(position)) {
                        R.layout.item_album -> ALBUM_CELL_SPAN
                        else -> spanCount
                    }
            }
        }
    }

    private fun calculateAlbumsPerRow(): Int {
        val rowWidthPx = binding.root.measuredWidth
        val minItemWidthPx = resources.getDimensionPixelSize(R.dimen.li_album_min_size)
        return (rowWidthPx / minItemWidthPx).coerceAtLeast(MIN_ALBUM_ITEMS_PER_ROW)
    }

}