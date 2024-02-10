package me.naloaty.photoprism.features.albums.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.flowFragmentViewModel
import me.naloaty.photoprism.base.sessionViewModels
import me.naloaty.photoprism.databinding.FragmentAlbumsBinding
import me.naloaty.photoprism.features.albums.presentation.recycler.AlbumsAdapter
import me.naloaty.photoprism.features.common_ext.syncWithBottomNav
import me.naloaty.photoprism.features.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.features.common_ext.withLoadStateFooter
import me.naloaty.photoprism.features.common_recycler.LoadStateAdapter
import me.naloaty.photoprism.features.common_recycler.LoadStateRenderer
import me.naloaty.photoprism.features.common_recycler.initCommonError
import me.naloaty.photoprism.features.common_search.initSearch
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import me.naloaty.photoprism.navigation.navigateSafely
import timber.log.Timber

private const val MIN_ALBUMS_PER_ROW = 2
private const val ALBUM_CELL_SPAN = 1

class AlbumsFragment : BaseSessionFragment(R.layout.fragment_albums) {

    private val viewModel: AlbumsViewModel by sessionViewModels()
    private val binding: FragmentAlbumsBinding by viewBinding()
    private val bottomNavViewModel: BottomNavViewModel by flowFragmentViewModel()

    private val albumsPagingAdapter = AlbumsAdapter()
    private val albumsFooterAdapter = LoadStateAdapter()

    private val albumsConcatAdapter by lazy {
        albumsPagingAdapter.withLoadStateFooter(
            footer = albumsFooterAdapter,
            config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
        )
    }

    private var loadStateRenderer by viewLifecycleProperty<LoadStateRenderer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentWithSessionComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadStateRenderer = LoadStateRenderer(
            root = binding.root,
            emptyView = binding.emptyGroup.root,
            loadingView = binding.loadingGroup.root,
            errorView = binding.errorGroup.root,
            contentView = binding.rvAlbums,
            onFallbackToCache = {
                Toast.makeText(context, "Albums from cache", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvAlbums.post(::setupAlbumList)
        setupAlbumsSearch()
    }

    private fun setupAlbumList() = with(binding) {
        rvAlbums.layoutManager = createGridLayoutManager()
        rvAlbums.adapter = albumsConcatAdapter

        initCommonError(errorGroup, onRetry = albumsPagingAdapter::retry)

        albumsPagingAdapter.onItemClickListener = { album ->
            val directions = AlbumsFragmentDirections.actionViewAlbumContent(album.uid)
            findNavController().navigateSafely(directions)
        }

        viewLifecycleOwner.lifecycleScope.run {
            launch {
                viewModel.searchQueryResult.collectLatest {
                    albumsPagingAdapter.submitData(lifecycle, it)
                }
            }

            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    launch {
                        albumsPagingAdapter.loadStateFlow.collectLatest { state ->
                            loadStateRenderer.update(state, albumsPagingAdapter.itemCount)
                        }
                    }

                    launch {
                        loadStateRenderer.render()
                    }
                }
            }
        }

        rvAlbums.syncWithBottomNav(bottomNavViewModel)
    }

    private fun setupAlbumsSearch() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                initSearch(
                    searchView = searchView,
                    searchBar = searchBar,
                    applyButton = searchViewContent.btnApply,
                    resetButton = searchViewContent.btnReset,
                    searchViewModel = viewModel,
                    bottomNavViewModel = bottomNavViewModel,
                    loadStateRenderer = loadStateRenderer
                )
            }
        }
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        val itemsPerRow = calculateAlbumsPerRow()

        return GridLayoutManager(context, itemsPerRow).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (albumsConcatAdapter.getItemViewType(position)) {
                        R.layout.li_album -> ALBUM_CELL_SPAN
                        else -> itemsPerRow
                    }
            }
        }
    }


    private fun calculateAlbumsPerRow(): Int {
        val rowWidthPx = binding.rvAlbums.measuredWidth

        val minAlbumWidthPx = resources.getDimensionPixelSize(
            R.dimen.li_album_min_size
        )

        val albumsPerRow = (rowWidthPx / minAlbumWidthPx)
            .coerceAtLeast(MIN_ALBUMS_PER_ROW)

        Timber.d(buildString {
            appendLine("albumsRow=$albumsPerRow")
            appendLine("rowWidthPx=$rowWidthPx")
            appendLine("minAlbumWidthPx=$minAlbumWidthPx")
        })
        return albumsPerRow
    }
}