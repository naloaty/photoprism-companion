package me.naloaty.photoprism.features.gallery.presentation

import android.os.Bundle
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.sessionFlowFragmentViewModel
import me.naloaty.photoprism.databinding.FragmentGalleryBinding
import me.naloaty.photoprism.features.common_ext.syncWithBottomNav
import me.naloaty.photoprism.features.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.features.common_ext.withLoadStateFooter
import me.naloaty.photoprism.features.common_recycler.LoadStateAdapter
import me.naloaty.photoprism.features.common_recycler.LoadStateRenderer
import me.naloaty.photoprism.features.common_recycler.initCommonError
import me.naloaty.photoprism.features.common_search.initSearch
import me.naloaty.photoprism.features.gallery.presentation.mapper.toGalleryListItem
import me.naloaty.photoprism.features.gallery.presentation.recycler.GalleryAdapter
import me.naloaty.photoprism.features.gallery.presentation.recycler.MediaItemViewHolder
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import me.naloaty.photoprism.navigation.navigateSafely
import timber.log.Timber

private const val MIN_MEDIA_ITEMS_PER_ROW = 1
private const val GALLERY_CELL_SPAN = 1

class GalleryFragment : BaseSessionFragment(R.layout.fragment_gallery) {

    private val args: GalleryFragmentArgs by navArgs()

    private val galleryViewModel: GalleryViewModel by sessionFlowFragmentViewModel()
    private val bottomNavViewModel: BottomNavViewModel by sessionFlowFragmentViewModel()

    private val binding: FragmentGalleryBinding by viewBinding()

    private var galleryPagingAdapter: GalleryAdapter by viewLifecycleProperty()
    private var loadStateRenderer: LoadStateRenderer by viewLifecycleProperty()


    private val galleryConcatAdapter by lazy {
        galleryPagingAdapter.withLoadStateFooter(
            footer = LoadStateAdapter(),
            config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryPagingAdapter = GalleryAdapter()

        setupGallerySearch()
        scrollToSharedElement()
        binding.rvGallery.post { setupGalleryList(args.albumUid) }

        loadStateRenderer = LoadStateRenderer(
            root = binding.root,
            emptyView = binding.emptyGroup.root,
            loadingView = binding.loadingGroup.root,
            errorView = binding.errorGroup.root,
            contentView = binding.rvGallery,
            onFallbackToCache = {
                Toast.makeText(context, "Gallery from cache", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun scrollToSharedElement() {
        binding.rvGallery.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.rvGallery.removeOnLayoutChangeListener(this)

                val sharedElementPosition = galleryViewModel.sharedElementPosition
                val layoutManager = binding.rvGallery.layoutManager ?: return
                val viewAtPosition = layoutManager.findViewByPosition(sharedElementPosition)

                val scrollNeeded = viewAtPosition == null ||
                        layoutManager.isViewPartiallyVisible(viewAtPosition, false, true)

                if (scrollNeeded) {
                    binding.rvGallery.run {
                        post { scrollToPosition(sharedElementPosition) }
                    }
                }
            }
        })
    }

    private fun setupSharedElementTransition() {
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                if (names.isEmpty()) return

                val viewHolder = binding.rvGallery.findViewHolderForAdapterPosition(
                    galleryViewModel.sharedElementPosition
                ) ?: return Timber.d("Could not find ViewHolder for shared element")

                val mediaItemHolder = viewHolder as? MediaItemViewHolder ?: return Timber.d(
                    "Unexpected shared element view type: ${viewHolder::class.simpleName}"
                )

                mediaItemHolder.sharedElement?.let {
                    sharedElements[names[0]] = it
                }
            }
        })
    }

    private fun setupGalleryList(albumUid: String?) = with(binding) {
        galleryPagingAdapter.onItemClickListener = { media ->
            val directions = GalleryFragmentDirections.actionViewMedia(media.uid)
            findNavController().navigateSafely(directions)
        }

        rvGallery.adapter = galleryConcatAdapter
        rvGallery.layoutManager = createGridLayoutManager()

        albumUid?.let { albumUid ->
            galleryViewModel.setAlbumFilter(albumUid)
            Timber.d("albumUid=$albumUid")
        }

        initCommonError(errorGroup, onRetry = galleryPagingAdapter::retry)

        viewLifecycleOwner.lifecycleScope.run {
            launch {
                galleryViewModel.searchQueryResult.collectLatest {
                    val galleryData = it.map { mediaItem -> mediaItem.toGalleryListItem() }
                    galleryPagingAdapter.submitData(galleryData)
                }
            }

            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    launch {
                        galleryPagingAdapter.loadStateFlow.collectLatest { state ->
                            loadStateRenderer.update(state, galleryPagingAdapter.itemCount)
                        }
                    }

                    launch {
                        loadStateRenderer.render()
                    }
                }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
                            resources.getString(
                                R.string.hint_album_content_search, title.lowercase()
                            )
                        }

                        searchBar.hint = hint
                        searchView.hint = hint
                    }
                }
            }
        }
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        val itemsPerRow = calculateMediaItemsPerRow()

        return GridLayoutManager(context, itemsPerRow).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (galleryPagingAdapter.getItemViewType(position)) {
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