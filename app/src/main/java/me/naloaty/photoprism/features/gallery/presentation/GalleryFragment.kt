package me.naloaty.photoprism.features.gallery.presentation

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.search.SearchView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.flowFragmentViewModel
import me.naloaty.photoprism.base.sessionViewModels
import me.naloaty.photoprism.common.extension.withLoadStateFooter
import me.naloaty.photoprism.common.recycler.LoadStateAdapter
import me.naloaty.photoprism.databinding.FragmentGalleryBinding
import me.naloaty.photoprism.features.gallery.presentation.recycler.GalleryAdapter
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import timber.log.Timber
import javax.inject.Inject


class GalleryFragment : BaseSessionFragment(R.layout.fragment_gallery) {

    private val viewModel: GalleryViewModel by sessionViewModels()
    private val viewBinding: FragmentGalleryBinding by viewBinding()
    private val args: GalleryFragmentArgs by navArgs()
    private val flowViewModel: BottomNavViewModel by flowFragmentViewModel()

    @Inject lateinit var galleryPagingAdapter: GalleryAdapter
    @Inject lateinit var galleryFooterAdapter: LoadStateAdapter

    private val galleryConcatAdapter by lazy {
        galleryPagingAdapter.withLoadStateFooter(
            footer = galleryFooterAdapter,
            config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentWithSessionComponent.inject(this)
    }

    override fun onStart() {
        super.onStart()
        setupGallerySearch()
        viewBinding.rvGallery.post(::setupGalleryList)
    }


    private fun setupGalleryList() = with(viewBinding) {
        rvGallery.layoutManager = createGridLayoutManager()
        rvGallery.adapter = galleryConcatAdapter

        args.albumUid?.let { albumUid ->
            viewModel.setAlbumFilter(albumUid)
            Timber.d("albumUid=$albumUid")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchQueryResult.collectLatest {
                galleryPagingAdapter.submitData(lifecycle, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            galleryPagingAdapter.loadStateFlow.collectLatest { loadState ->
                when (val currentState = loadState.mediator?.refresh) {
                    is LoadState.Error -> {
                        currentState.error
                    }

                    else -> {}
                }
            }
        }

        /**
         * TODO: Implement custom ViewHelper in order to support PagingDataAdapter
         */

//        val viewHelper = PagingRecyclerViewHelper(rvGallery)
//
//        viewLifecycleOwner.lifecycleScope.launch(dispatchers.io) {
//            viewModel.searchQueryResultCount.collectLatest {
//                viewHelper.updateItemCount(it)
//            }
//        }
//
//        FastScrollerBuilder(rvGallery)
//            .setViewHelper(viewHelper)
//            .useMd2Style()
//            .build()

        setupScrollingBehavior()
    }

    private fun setupScrollingBehavior() = with(viewBinding) {
        rvGallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { // Scrolling down
                    flowViewModel.onScrollingDown()
                } else { // Scrolling up
                    flowViewModel.onScrollingUp()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (
                    RecyclerView.SCROLL_STATE_DRAGGING == newState ||
                    RecyclerView.SCROLL_STATE_SETTLING == newState
                ) {
                    flowViewModel.onListStateChanged(true)
                } else {
                    flowViewModel.onListStateChanged(false)
                }
            }
        })
    }

    private fun setupGallerySearch() = with(viewBinding) {
        searchView.editText.addTextChangedListener {
            viewModel.onSearchTextChanged(it.toString())
        }

        searchView.addTransitionListener { _, previousState, newState ->
            if (
                SearchView.TransitionState.HIDING == previousState &&
                SearchView.TransitionState.HIDDEN == newState
            ) {
                viewModel.onSearchViewHidden()
                flowViewModel.onSearchViewHidden()
            }

            if (SearchView.TransitionState.SHOWING == newState) {
                flowViewModel.onSearchViewShowing()
            }
        }

        searchViewContent.btnApply.setOnClickListener {
            searchBar.text = searchView.text
            viewModel.onApplySearch()
            searchView.hide()
        }

        searchViewContent.btnReset.setOnClickListener {
            searchBar.clearText()
            searchView.clearText()
            viewModel.onResetSearch()
            searchView.hide()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.albumTitle.collectLatest { title ->
                val hint = if (title == null) {
                    resources.getString(R.string.hint_media_library_search)
                } else {
                    resources.getString(R.string.hint_album_content_search, title.lowercase())
                }

                searchBar.hint = hint
                searchView.hint = hint
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.applySearchButtonEnabled.collectLatest {
                searchViewContent.btnApply.isEnabled = it
            }
        }
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        val itemsPerRow = calculateMediaItemsPerRow()

        return GridLayoutManager(context, itemsPerRow).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (galleryConcatAdapter.getItemViewType(position)) {
                        R.layout.li_gallery_media_item -> GALLERY_CELL_SPAN
                        else -> itemsPerRow
                    }
            }
        }
    }


    private fun calculateMediaItemsPerRow(): Int {
        val rowWidthPx = viewBinding.rvGallery.measuredWidth

        val minItemWidthPx = resources.getDimensionPixelSize(
            R.dimen.li_gallery_media_item_min_size
        )

        val itemsPerRow = (rowWidthPx / minItemWidthPx)
            .coerceAtLeast(MIN_MEDIA_ITEMS_PER_ROW)

        Timber.d(buildString {
            appendLine("itemsPerRow=$itemsPerRow")
            appendLine("rowWidthPx=$rowWidthPx")
            appendLine("minItemWidthPx=$minItemWidthPx")
        })
        return itemsPerRow
    }

    companion object {
        const val MIN_MEDIA_ITEMS_PER_ROW = 1
        const val GALLERY_CELL_SPAN = 1
    }

}