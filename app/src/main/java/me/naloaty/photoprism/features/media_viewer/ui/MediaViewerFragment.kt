package me.naloaty.photoprism.features.media_viewer.ui

import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.storeViaViewModel
import me.naloaty.photoprism.databinding.FragmentMediaViewerBinding
import me.naloaty.photoprism.features.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryStore
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryUiEvent
import me.naloaty.photoprism.features.gallery_v2.ui.GALLERY_STORE_KEY
import me.naloaty.photoprism.features.media_viewer.ui.model.PagerUiState
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.SimpleTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory

class MediaViewerFragment: BaseSessionFragment(R.layout.fragment_media_viewer) {

    private val binding by viewBinding(FragmentMediaViewerBinding::bind)
    private val args: MediaViewerFragmentArgs by navArgs()
    private val component by lazy { sessionFragmentComponent.mediaViewerComponent() }
    private var adapter: BaseTiAdapter<ViewTyped, CoroutinesHolderFactory> by viewLifecycleProperty()

    private val galleryStore by storeViaViewModel<GalleryStore>(
        sharedViewModelKey = { args.albumUid ?: GALLERY_STORE_KEY },
        ownerProducer = { requireParentFragment().requireParentFragment() }
    ) {
        throw IllegalStateException("Shared gallery store is not found")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryStore.collectOnCreate(
            fragment = this,
            uiStateMapper = component.pagerUiStateMapper,
            stateCollector = ::collectPagerState
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        //setupSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            //postponeEnterTransition();
        }
    }

    private fun setupViewPager() = with(binding) {
        adapter = SimpleTiAdapter(MediaViewerHolderFactory())
        viewPager.adapter = adapter

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

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                galleryStore.dispatch(GalleryUiEvent.OnLoadMore(position))
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

}