package me.naloaty.photoprism.features.albums.presentation

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.search.SearchView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.flowFragmentViewModel
import me.naloaty.photoprism.base.sessionViewModels
import me.naloaty.photoprism.features.common_ext.withLoadStateFooter
import me.naloaty.photoprism.features.common_recycler.LoadStateAdapter
import me.naloaty.photoprism.databinding.FragmentAlbumsBinding
import me.naloaty.photoprism.features.albums.presentation.recycler.AlbumsAdapter
import me.naloaty.photoprism.features.common_ext.syncWithNavBottom
import me.naloaty.photoprism.navigation.main.BottomNavViewModel
import me.naloaty.photoprism.navigation.navigateSafely
import timber.log.Timber

private const val MIN_ALBUMS_PER_ROW = 2

class AlbumsFragment : BaseSessionFragment(R.layout.fragment_albums) {

    private val viewModel: AlbumsViewModel by sessionViewModels()
    private val viewBinding: FragmentAlbumsBinding by viewBinding()
    private val bottomNavViewModel: BottomNavViewModel by flowFragmentViewModel()

    private val albumsPagingAdapter = AlbumsAdapter()
    private val albumsFooterAdapter = LoadStateAdapter()

    private val galleryConcatAdapter by lazy {
        albumsPagingAdapter.withLoadStateFooter(
            footer = albumsFooterAdapter,
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
        viewBinding.rvAlbums.post(::setupAlbumList)
        setupAlbumsSearch()
    }

    private fun setupAlbumList() = with(viewBinding) {
        rvAlbums.layoutManager = GridLayoutManager(context, calculateAlbumsPerRow())
        rvAlbums.adapter = galleryConcatAdapter

        albumsPagingAdapter.onItemClickListener = { album ->
            val directions = AlbumsFragmentDirections.actionViewAlbumContent(album.uid)
            findNavController().navigateSafely(directions)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchQueryResult.collectLatest {
                albumsPagingAdapter.submitData(lifecycle, it)
            }
        }

        rvAlbums.syncWithNavBottom(bottomNavViewModel)
    }

    private fun setupAlbumsSearch() = with(viewBinding) {
        searchView.editText.addTextChangedListener {
            viewModel.onSearchTextChanged(it.toString())
        }

        searchView.addTransitionListener { _, previousState, newState ->
            if (
                SearchView.TransitionState.HIDING == previousState &&
                SearchView.TransitionState.HIDDEN == newState
            ) {
                viewModel.onSearchViewHidden()
                bottomNavViewModel.onSearchViewHidden()
            }

            if (SearchView.TransitionState.SHOWING == newState) {
                bottomNavViewModel.onSearchViewShowing()
            }
        }

        searchViewContent.btnApply.setOnClickListener {
            searchBar.setText(searchView.text)
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
            viewModel.applySearchButtonEnabled.collectLatest {
                searchViewContent.btnApply.isEnabled = it
            }
        }
    }

    private fun calculateAlbumsPerRow(): Int {
        val rowWidthPx = viewBinding.rvAlbums.measuredWidth

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