package me.naloaty.photoprism.features.media_viewer.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.util.ViewPreloadSizeProvider
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.sessionFragmentViewModel
import me.naloaty.photoprism.base.storeViaViewModel
import me.naloaty.photoprism.common.common_ext.addOnBackPressedCallback
import me.naloaty.photoprism.common.common_ext.clickedItem
import me.naloaty.photoprism.common.common_ext.finishWithResult
import me.naloaty.photoprism.common.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.common.common_kotea.ContextResourcesProvider
import me.naloaty.photoprism.common.common_paging.model.data
import me.naloaty.photoprism.common.common_recycler.doOnPopulate
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.common.common_viewmodel.sessionViewModelStoreOwner
import me.naloaty.photoprism.common.common_viewpager.ViewPagerPreloader
import me.naloaty.photoprism.databinding.FragmentMediaViewerBinding
import me.naloaty.photoprism.di.injector.sessionComponentOwner
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryStore
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUiEvent
import me.naloaty.photoprism.features.gallery.ui.GALLERY_STORE_KEY
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerUiEvent.OnItemsUpdate
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerUiEvent.OnPositionChanged
import me.naloaty.photoprism.features.media_viewer.ui.model.MediaViewerUiState
import me.naloaty.photoprism.features.media_viewer.ui.model.PagerUiState
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.kotea.android.ui.map
import ru.tinkoff.mobile.tech.ti_recycler.adapters.AsyncTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.diff.ViewTypedDiffCallback
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory
import timber.log.Timber

private const val PRELOAD_AHEAD_SIZE = 2

class MediaViewerFragment: BaseSessionFragment(R.layout.fragment_media_viewer) {
    private val binding by viewBinding(FragmentMediaViewerBinding::bind)
    private val args: MediaViewerFragmentArgs by navArgs()

    private val component by sessionComponentOwner {
        mediaViewerComponentFactory().create()
    }

    private val galleryStore by storeViaViewModel<GalleryStore>(
        sharedViewModelKey = { getSharedKey(GALLERY_STORE_KEY) },
        ownerProducer = { sessionViewModelStoreOwner }
    ) {
        throw IllegalStateException("Shared gallery store is not found")
    }

    private val viewerStore by storeViaViewModel { component.mediaViewerStore }

    private var adapter: BaseTiAdapter<ViewTyped, CoroutinesHolderFactory> by viewLifecycleProperty()
    private val requestManager by lazy { Glide.with(this) }
    private val videoPlayerCacheViewModel: VideoPlayerCacheViewModel by sessionFragmentViewModel()

    private var lastItemUid: String = ""

    private fun getSharedKey(tag: String): String {
        return if (args.albumUid != null) {
            tag + "_" + args.albumUid
        } else {
            tag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resourcesProvider = ContextResourcesProvider(requireContext())
        val pagerUiStateMapper = component.pagerUiStateMapper

        viewerStore.collectOnCreate(
            fragment = this,
            uiStateMapper = component.mediaViewerUiStateMapper,
            stateCollector = ::collectMediaViewerState
        )

        galleryStore.collectOnCreate(
            lifecycleOwner = this,
            stateCollector = {
                viewerStore.dispatch(OnItemsUpdate(it.listState.data))

                collectPagerState(
                    pagerUiStateMapper.map(resourcesProvider, it)
                )
            }
        )

        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        addOnBackPressedCallback { navigateBack() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(savedInstanceState)
        initToolbar()
        //setupSharedElementTransition()
    }

    private fun setupViewPager(savedInstanceState: Bundle?) = with(binding) {
        adapter = AsyncTiAdapter(
            holderFactory = MediaViewerHolderFactory(
                videoPlayerCache = videoPlayerCacheViewModel,
                requestManager = requestManager
            ),
            diffItemCallback = ViewTypedDiffCallback()
        )

        adapter.doOnPopulate {
            viewPager.adapter = adapter

            if (savedInstanceState == null) {
                scrollToInitialPosition()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.clickedItem<CommonErrorItem>(R.layout.item_common_error)
                .map { GalleryUiEvent.OnClickRestart }
                .collect(galleryStore::dispatch)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Timber.d("onPageSelected = $position")
                galleryStore.dispatch(GalleryUiEvent.OnLoadMore(position))
                viewerStore.dispatch(OnPositionChanged(position))
                lastItemUid = adapter.items[position].uid
            }
        })

        viewPager.setPageTransformer(
            MarginPageTransformer(
                resources.getDimensionPixelSize(R.dimen.media_viewer_page_margin)
            )
        )

        initImageItemsPreloader()
    }

    private fun initImageItemsPreloader() {
        val preloadModelProvider = MediaViewerPreloadModelProvider(requestManager, adapter)
        val preloadSizeProvider = ViewPreloadSizeProvider<ViewTyped>(binding.viewPager)

        val preloader = ViewPagerPreloader(
            itemCountProvider = { adapter.items.size },
            requestManager = requestManager,
            preloadModelProvider = preloadModelProvider,
            preloadDimensionProvider = preloadSizeProvider,
            maxPreload = PRELOAD_AHEAD_SIZE,
        )

       binding.viewPager.registerOnPageChangeCallback(preloader)
    }

    private fun scrollToInitialPosition() = with(binding) {
        val initialPosition = adapter.items
            .indexOfFirst { it.uid == args.mediaUid }
            .takeIf { it != -1 } ?: 0

        Timber.d("Scroll to initial position: $initialPosition")
        viewPager.setCurrentItem(initialPosition, false)
        startPostponedEnterTransition()
    }

    private fun setupSharedElementTransition() {
        setEnterSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
            }
        })
    }

    private fun initToolbar() {
        binding.toolbar.run {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { navigateBack() }
        }
    }

    private fun navigateBack() {
        findNavController().finishWithResult(
            Result(lastItemUid)
        )
    }


    private fun collectPagerState(state: PagerUiState) {
        adapter.items = state.pagerItems
    }

    private fun collectMediaViewerState(state: MediaViewerUiState) {
        with(binding.toolbar) {
            title = state.title
            subtitle = state.subtitle
        }
    }

    @Parcelize
    data class Result(val lastItemUid: String) : Parcelable

}