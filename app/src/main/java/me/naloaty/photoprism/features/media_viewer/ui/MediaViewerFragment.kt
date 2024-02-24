package me.naloaty.photoprism.features.media_viewer.ui

import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.storeViaViewModel
import me.naloaty.photoprism.common.common_ext.clickedItem
import me.naloaty.photoprism.common.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.databinding.FragmentMediaViewerBinding
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryStore
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryUiEvent
import me.naloaty.photoprism.features.gallery_v2.ui.GALLERY_STORE_KEY
import me.naloaty.photoprism.features.media_viewer.ui.model.MediaViewerUiState
import me.naloaty.photoprism.features.media_viewer.ui.model.PagerUiState
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.mobile.tech.ti_recycler.adapters.AsyncTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.diff.ViewTypedDiffCallback
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory
import timber.log.Timber

class MediaViewerFragment: BaseSessionFragment(R.layout.fragment_media_viewer) {

    private val binding by viewBinding(FragmentMediaViewerBinding::bind)
    private val args: MediaViewerFragmentArgs by navArgs()

    private val component by lazy {
        sessionFragmentComponent.mediaViewerComponentFactory()
            .create(initialPosition = args.position)
    }

    private var adapter: BaseTiAdapter<ViewTyped, CoroutinesHolderFactory> by viewLifecycleProperty()

    private val galleryStore by storeViaViewModel<GalleryStore>(
        sharedViewModelKey = { getSharedKey(GALLERY_STORE_KEY) },
        ownerProducer = { requireParentFragment().requireParentFragment() }
    ) {
        throw IllegalStateException("Shared gallery store is not found")
    }

    private val viewerStore by storeViaViewModel {
        component.mediaViewerStore
    }

    private fun getSharedKey(tag: String): String {
        return if (args.albumUid != null) {
            tag + "_" + args.albumUid
        } else {
            tag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryStore.collectOnCreate(
            fragment = this,
            uiStateMapper = component.pagerUiStateMapper,
            stateCollector = ::collectPagerState
        )
        viewerStore.collectOnCreate(
            fragment = this,
            uiStateMapper = component.mediaViewerUiStateMapper,
            stateCollector = ::collectMediaViewerState
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        //setupSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            scrollToInitialPosition()
        }
    }

    private fun setupViewPager() = with(binding) {
        adapter = AsyncTiAdapter(MediaViewerHolderFactory(), ViewTypedDiffCallback())
        viewPager.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.clickedItem<CommonErrorItem>(R.layout.layout_common_error)
                .map { GalleryUiEvent.OnClickRestart }
                .collect(galleryStore::dispatch)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Timber.d("ViewPager page selected: $position")
                galleryStore.dispatch(GalleryUiEvent.OnLoadMore(position))
            }
        })
    }

    private fun scrollToInitialPosition() = with(binding) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (args.position == position) {
                    viewPager.isVisible = true

                    viewPager.post {
                        viewPager.unregisterOnPageChangeCallback(this)
                    }
                } else {
                    viewPager.visibility = View.INVISIBLE
                }
            }
        })

        viewPager.run {
            post { setCurrentItem(args.position, false) }
        }
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

    private fun collectPagerState(state: PagerUiState) {
        adapter.items = state.pagerItems
    }

    private fun collectMediaViewerState(state: MediaViewerUiState) {
    }

}