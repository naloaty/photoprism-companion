package me.naloaty.photoprism.features.gallery.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.databinding.FragmentGalleryBinding
import me.naloaty.photoprism.features.common_ext.syncWithBottomNav
import me.naloaty.photoprism.features.common_ext.withLoadStateFooter
import me.naloaty.photoprism.features.common_recycler.LoadStateAdapter
import me.naloaty.photoprism.features.common_recycler.LoadStateRenderer
import me.naloaty.photoprism.features.common_recycler.initCommonError
import me.naloaty.photoprism.features.common_search.initSearch
import me.naloaty.photoprism.features.gallery.presentation.recycler.GalleryAdapter
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import timber.log.Timber

private const val MIN_MEDIA_ITEMS_PER_ROW = 1
private const val GALLERY_CELL_SPAN = 1

class GalleryRenderer(
    private val context: Context,
    private val binding: FragmentGalleryBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val galleryViewModel: GalleryViewModel,
    private val bottomNavViewModel: BottomNavViewModel
) {
    private val resources = context.resources

    private val galleryPagingAdapter = GalleryAdapter()
    private val galleryFooterAdapter = LoadStateAdapter()

    // Flag
    private val loadStateRenderer = LoadStateRenderer(
        root = binding.root,
        emptyView = binding.emptyGroup.root,
        loadingView = binding.loadingGroup.root,
        errorView = binding.errorGroup.root,
        contentView = binding.rvGallery,
        onFallbackToCache = {
            Toast.makeText(context, "Gallery from cache", Toast.LENGTH_SHORT).show()
        }
    )

    // Flag
    private val galleryConcatAdapter by lazy {
        galleryPagingAdapter.withLoadStateFooter(
            footer = galleryFooterAdapter,
            config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
        )
    }

    fun init(albumUid: String?) {
        setupGallerySearch()
        binding.rvGallery.post { setupGalleryList(albumUid) }
    }


    private fun setupGalleryList(albumUid: String?) = with(binding) {
        rvGallery.layoutManager = createGridLayoutManager()
        rvGallery.adapter = galleryConcatAdapter

        albumUid?.let { albumUid ->
            galleryViewModel.setAlbumFilter(albumUid)
            Timber.d("albumUid=$albumUid")
        }

        initCommonError(errorGroup, onRetry = galleryPagingAdapter::retry)

        viewLifecycleOwner.lifecycleScope.run {
            launch {
                galleryViewModel.searchQueryResult.collectLatest {
                    galleryPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }

            launch {
                galleryPagingAdapter.loadStateFlow.collectLatest { state ->
                    loadStateRenderer.update(state, galleryPagingAdapter.itemCount)
                }
            }

            launch {
                loadStateRenderer.render()
            }
        }

        /**
         * TODO: Implement custom ViewHelper in order to support PagingDataAdapter
         */

//        val viewHelper = PagingRecyclerViewHelper(rvGallery)
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            galleryViewModel.searchQueryResultCount.collectLatest {
//                viewHelper.updateItemCount(it)
//            }
//        }
//
//        FastScrollerBuilder(rvGallery)
//            .setViewHelper(viewHelper)
//            .useMd2Style()
//            .build()

        rvGallery.syncWithBottomNav(bottomNavViewModel)
    }

    private fun setupGallerySearch() = with(binding) {
        viewLifecycleOwner.lifecycleScope.run {
            initSearch(
                searchView = searchView,
                searchBar = searchBar,
                applyButton = searchViewContent.btnApply,
                resetButton = searchViewContent.btnReset,
                searchViewModel = galleryViewModel,
                bottomNavViewModel = bottomNavViewModel,
                loadStateRenderer = loadStateRenderer
            )

            launch {
                galleryViewModel.albumTitle.collectLatest { title ->
                    val hint = if (title == null) {
                        resources.getString(R.string.hint_media_library_search)
                    } else {
                        resources.getString(R.string.hint_album_content_search, title.lowercase())
                    }

                    searchBar.hint = hint
                    searchView.hint = hint
                }
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
        val rowWidthPx = binding.rvGallery.measuredWidth

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

}