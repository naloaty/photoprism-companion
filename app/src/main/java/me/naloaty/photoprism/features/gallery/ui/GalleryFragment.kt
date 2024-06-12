package me.naloaty.photoprism.features.gallery.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.collection.MutableScatterMap
import androidx.collection.ScatterMap
import androidx.core.view.doOnLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseFragment
import me.naloaty.photoprism.base.sessionFlowFragmentViewModel
import me.naloaty.photoprism.base.storeViaViewModel
import me.naloaty.photoprism.common.common_ext.addOnBackPressedCallback
import me.naloaty.photoprism.common.common_ext.ensureItemIsVisible
import me.naloaty.photoprism.common.common_ext.handleResult
import me.naloaty.photoprism.common.common_ext.syncWithBottomNav
import me.naloaty.photoprism.common.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.common.common_recycler.FlushableAsyncTiAdapter
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.common.common_recycler.model.CommonNextPageErrorItem
import me.naloaty.photoprism.common.common_recycler.pagingEndlessScrollFlow
import me.naloaty.photoprism.common.common_viewmodel.sessionViewModelStoreOwner
import me.naloaty.photoprism.databinding.FragmentGalleryBinding
import me.naloaty.photoprism.di.injector.sessionComponentOwner
import me.naloaty.photoprism.features.gallery.presentation.GalleryConfig
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryNews
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUiEvent
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUiEvent.OnPerformSearch
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchNews
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchNews.HideSearchView
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchNews.PerformSearch
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchUiEvent
import me.naloaty.photoprism.features.gallery.ui.items.MediaThumbnailItem
import me.naloaty.photoprism.features.gallery.ui.model.GallerySearchUiState
import me.naloaty.photoprism.features.gallery.ui.model.GalleryUiState
import me.naloaty.photoprism.features.media_viewer.ui.MediaViewerFragment
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import me.naloaty.photoprism.navigation.navigateSafely
import me.naloaty.photoprism.util.EMPTY_STRING
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.diff.ViewTypedDiffCallback
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.TiRecyclerCoroutines
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory
import timber.log.Timber


private const val MIN_MEDIA_ITEMS_PER_ROW = 1
private const val GALLERY_CELL_SPAN = 1
private const val PRELOAD_AHEAD_SIZE = 20

const val GALLERY_STORE_KEY = "gallery"
const val GALLERY_SEARCH_STORE_KEY = "gallery_search"

class GalleryFragment : BaseFragment(R.layout.fragment_gallery) {

    private val args: GalleryFragmentArgs by navArgs()
    private val binding: FragmentGalleryBinding by viewBinding()

    private val component by sessionComponentOwner(
        componentTag = { getSharedKey(GALLERY_STORE_KEY) }
    ) {
        galleryComponentFactory().create(
            config = GalleryConfig(
                albumUid = args.albumUid,
            )
        )
    }

    private val store by storeViaViewModel(
        sharedViewModelKey = { getSharedKey(GALLERY_STORE_KEY) },
        ownerProducer = { sessionViewModelStoreOwner }
    ) {
        component.galleryStore
    }

    private val searchStore by storeViaViewModel(
        sharedViewModelKey = { getSharedKey(GALLERY_SEARCH_STORE_KEY) },
        ownerProducer = { sessionViewModelStoreOwner }
    ) {
        component.gallerySearchStore
    }

    private val bottomNavViewModel: BottomNavViewModel by sessionFlowFragmentViewModel()

    private val requestManager by lazy { Glide.with(requireActivity()) }
    private var adapter: FlushableAsyncTiAdapter<ViewTyped, CoroutinesHolderFactory> by viewLifecycleProperty()
    private var tiRecycler: TiRecyclerCoroutines<ViewTyped> by viewLifecycleProperty()
    private var itemPositions: ScatterMap<String, Int> by viewLifecycleProperty()

    private fun getSharedKey(tag: String): String {
        return if (args.albumUid != null) {
            tag + "_" + args.albumUid
        } else {
            tag
        }
    }

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

        setupBackNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSearch()
        initRecyclerAdapter()
        initRecyclerView()
        initNavigationResultListener()
    }

    private fun setupBackNavigation() {
        addOnBackPressedCallback {
            when {
                binding.searchView.isShowing -> binding.searchView.hide()
                binding.searchView.text.isNotEmpty() -> searchStore.dispatch(GallerySearchUiEvent.OnResetSearch)
                else -> findNavController().navigateUp()
            }
        }
    }

    private fun initNavigationResultListener() {
        if (args.albumUid == null) {
            findNavController().handleResult<MediaViewerFragment.Result>(
                viewLifecycleOwner,
                R.id.gallery_fragment,
                R.id.media_view_fragment
            ) { result -> scrollToMediaViewerPosition(result.lastItemUid)}
        } else {
            findNavController().handleResult<MediaViewerFragment.Result>(
                viewLifecycleOwner,
                R.id.album_content_fragment,
                R.id.media_view_fragment
            ) { result -> scrollToMediaViewerPosition(result.lastItemUid)}
        }
    }

    private fun scrollToMediaViewerPosition(itemUid: String) {
        tiRecycler.recyclerView.run {
            post {
                itemPositions[itemUid]?.let {
                    Timber.d(buildString {
                        appendLine("root.top = ${binding.root.top}")
                        appendLine("root.measuredHeight = ${binding.root.measuredHeight}")
                        appendLine("root.bottom = ${binding.root.bottom}")
                    })
                    ensureItemIsVisible(
                        itemGlobalPosition = it,
                        topOffset = binding.appBarLayout.measuredHeight,
                        bottomOffset = binding.appBarLayout.measuredHeight
                    )
                }
            }
        }
    }

    private fun initRecyclerAdapter() {
        adapter = FlushableAsyncTiAdapter(
            holderFactory = GalleryViewHolderFactory(requestManager),
            diffItemCallback = ViewTypedDiffCallback()
        )
    }

    private fun initRecyclerView() {
        val gridLayoutManager = createGridLayoutManager()

        binding.root.doOnLayout {
            gridLayoutManager.spanCount = calculateMediaItemsPerRow()
        }

        tiRecycler = TiRecyclerCoroutines(binding.rvGallery, adapter) {
            layoutManager = gridLayoutManager
        }

        viewLifecycleOwner.lifecycleScope.launch {
            merge(
                tiRecycler.clickedItem<MediaThumbnailItem>(R.layout.item_gallery_media_thumbnail)
                    .map { GalleryUiEvent.OnClickMediaItem(it.uid) },
                tiRecycler.clickedItem<CommonErrorItem>(R.layout.item_common_error)
                    .map { GalleryUiEvent.OnClickRestart },
                tiRecycler.clickedItem<CommonNextPageErrorItem>(R.layout.item_common_next_page_error)
                    .map { GalleryUiEvent.OnClickRestart },
                tiRecycler.recyclerView.pagingEndlessScrollFlow()
                    .map { GalleryUiEvent.OnLoadMore(it) },
            ).onEach {
                Timber.d("UiEvent: $it")
            }.collect(store::dispatch)
        }

        //tiRecycler.recyclerView.syncWithBottomNav(bottomNavViewModel)
        initImageItemsPreloader()
    }

    private fun initImageItemsPreloader() {
        val preloadModelProvider = GalleryPreloadModelProvider(requestManager, tiRecycler.adapter)
        val preloadSizeProvider = ViewPreloadSizeProvider<ViewTyped>()
        val preloader = RecyclerViewPreloader(
            requestManager, preloadModelProvider, preloadSizeProvider, PRELOAD_AHEAD_SIZE
        )

        tiRecycler.recyclerView.addOnScrollListener(preloader)
        tiRecycler.recyclerView.setItemViewCacheSize(PRELOAD_AHEAD_SIZE / 3)
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

        if (args.albumUid != null) {
            searchBar.setNavigationIcon(R.drawable.ic_back)
            searchBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        searchView.syncWithBottomNav(bottomNavViewModel)
        appBarLayout.syncWithBottomNav(bottomNavViewModel)
    }

    private fun collectState(state: GalleryUiState) {
        adapter.items = state.listItems.also {
            itemPositions = MutableScatterMap<String, Int>().apply {
                it.forEachIndexed { index, item -> set(item.uid, index) }
            }
        }
    }

    private fun collectNews(news: GalleryNews) {
        when (news) {
            is GalleryNews.OpenPreview -> {
                val directions = GalleryFragmentDirections.actionViewMedia(
                    mediaUid = news.uid,
                    albumUid = args.albumUid
                )
                findNavController().navigateSafely(directions)
            }
        }
    }

    private fun collectSearchState(state: GallerySearchUiState) = with(binding) {
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

    private fun collectSearchNews(news: GallerySearchNews) = when (news) {
        is HideSearchView -> binding.searchView.hide()
        is PerformSearch -> store.dispatch(OnPerformSearch(news.query))
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        return GridLayoutManager(context, MIN_MEDIA_ITEMS_PER_ROW).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (adapter.getItemViewType(position)) {
                        R.layout.item_gallery_media_thumbnail -> GALLERY_CELL_SPAN
                        R.layout.item_gallery_media_placeholder -> GALLERY_CELL_SPAN
                        else -> spanCount
                    }
            }
        }
    }

    private fun calculateMediaItemsPerRow(): Int {
        val rowWidthPx = binding.root.measuredWidth
        val minItemWidthPx = resources.getDimensionPixelSize(R.dimen.li_gallery_media_item_min_size)
        return (rowWidthPx / minItemWidthPx).coerceAtLeast(MIN_MEDIA_ITEMS_PER_ROW)
    }

}