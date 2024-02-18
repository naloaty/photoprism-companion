package me.naloaty.photoprism.features.gallery_v2.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.sessionFlowFragmentViewModel
import me.naloaty.photoprism.databinding.FragmentGalleryBinding
import me.naloaty.photoprism.features.common_ext.syncWithBottomNav
import me.naloaty.photoprism.features.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.features.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.features.common_recycler.model.CommonNextPageErrorItem
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryNews
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryUiEvent
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchNews
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchNews.HideSearchView
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchNews.PerformSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUiEvent
import me.naloaty.photoprism.features.gallery_v2.ui.model.GallerySearchUiState
import me.naloaty.photoprism.features.gallery_v2.ui.model.GalleryUiState
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import me.naloaty.photoprism.util.EMPTY_STRING
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.diff.ViewTypedDiffCallback
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.TiRecyclerCoroutines
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.scroll.endlessScrollFlow


private const val MIN_MEDIA_ITEMS_PER_ROW = 1
private const val GALLERY_CELL_SPAN = 1

class GalleryFragment : BaseSessionFragment(R.layout.fragment_gallery) {

    //private val args: GalleryFragmentArgs by navArgs()
    private val binding: FragmentGalleryBinding by viewBinding()

    private val component by lazy {
        sessionFragmentComponent.galleryComponentFactory()
            .create(albumUid = EMPTY_STRING /* args.albumUid.orEmpty() */)
    }

    private val store by storeViaViewModel { component.galleryStore }
    private val searchStore by storeViaViewModel { component.gallerySearchStore }

    private val bottomNavViewModel: BottomNavViewModel by sessionFlowFragmentViewModel()

    private var adapter: RecyclerView.Adapter<*> by viewLifecycleProperty()
    private var tiRecycler: TiRecyclerCoroutines<ViewTyped> by viewLifecycleProperty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        searchStore.dispatch(GallerySearchUiEvent.OnApplySearch)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initSearch()
    }

    private fun initRecyclerView() {
        tiRecycler = TiRecyclerCoroutines(
            binding.rvGallery, GalleryViewHolderFactory(), ViewTypedDiffCallback()
        ) {
            layoutManager = createGridLayoutManager()
        }

        adapter = tiRecycler.adapter

        viewLifecycleOwner.lifecycleScope.launch {
            merge(
                tiRecycler.adapter.holderFactory.clickPosition(R.layout.li_gallery_media_item)
                    .map { GalleryUiEvent.OnClickMediaItem(it) },
                tiRecycler.clickedItem<CommonErrorItem>(R.layout.layout_common_error)
                    .map { GalleryUiEvent.OnClickRestart },
                tiRecycler.clickedItem<CommonNextPageErrorItem>(R.layout.layout_common_next_page_error)
                    .map { GalleryUiEvent.OnLoadMore },
                tiRecycler.recyclerView.endlessScrollFlow(pageSize = 10)
                    .map { GalleryUiEvent.OnLoadMore },
            ).collect(store::dispatch)
        }

        tiRecycler.recyclerView.syncWithBottomNav(bottomNavViewModel)
    }

    private fun initSearch() = with(binding) {
        searchView.editText.addTextChangedListener {
            searchStore.dispatch(
                GallerySearchUiEvent.OnSearchTextChanged(
                    it?.toString() ?: EMPTY_STRING
                )
            )
        }

        searchView.editText.setOnEditorActionListener { _, actionId, _ ->
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                searchStore.dispatch(GallerySearchUiEvent.OnApplySearch)
                true
            } else {
                false
            }
        }

        searchViewContent.btnApply.setOnClickListener {
            searchStore.dispatch(GallerySearchUiEvent.OnApplySearch)
        }

        searchViewContent.btnReset.setOnClickListener {
            searchStore.dispatch(GallerySearchUiEvent.OnResetSearch)
        }

        searchView.syncWithBottomNav(bottomNavViewModel)
    }

    private fun collectState(state: GalleryUiState) {
        tiRecycler.setItems(state.listItems)
    }

    private fun collectNews(news: GalleryNews) = when (news) {
        is GalleryNews.OpenPreview -> {
//            val directions = GalleryFragmentDirections.actionViewMedia(news.position)
//            findNavController().navigateSafely(directions)
        }
    }

    private fun collectSearchState(state: GallerySearchUiState) = with(binding) {
        searchViewContent.btnApply.isEnabled = state.applyBtnEnabled
        searchBar.hint = state.searchBarHint
        searchView.hint = state.searchBarHint
        searchBar.setText(state.searchBarText)
    }

    private fun collectSearchNews(news: GallerySearchNews) = when(news) {
        is HideSearchView -> binding.searchView.hide()
        is PerformSearch -> store.dispatch(GalleryUiEvent.OnPerformSearch(news.query))
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        val itemsPerRow = 3

        return GridLayoutManager(context, itemsPerRow).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (adapter.getItemViewType(position)) {
                        R.layout.li_gallery_media_item -> GALLERY_CELL_SPAN
                        else -> itemsPerRow
                    }
            }
        }
    }

    private fun calculateMediaItemsPerRow(): Int {
        val rowWidthPx = binding.root.measuredWidth
        val minItemWidthPx = resources.getDimensionPixelSize(
            R.dimen.li_gallery_media_item_min_size
        )
        return (rowWidthPx / minItemWidthPx).coerceAtLeast(MIN_MEDIA_ITEMS_PER_ROW)
    }

}