package me.naloaty.photoprism.features.media_viewer.ui

import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.map
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.sessionFlowFragmentViewModel
import me.naloaty.photoprism.databinding.FragmentMediaViewerBinding
import me.naloaty.photoprism.features.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.features.gallery.presentation.GalleryViewModel
import me.naloaty.photoprism.features.gallery.presentation.mapper.toGalleryListItem
import me.naloaty.photoprism.features.gallery.presentation.recycler.GalleryAdapter

class MediaViewerFragment: BaseSessionFragment(R.layout.fragment_media_viewer) {

    private val binding by viewBinding(FragmentMediaViewerBinding::bind)

    private val galleryViewModel: GalleryViewModel by sessionFlowFragmentViewModel()

    private var mediaViewerAdapter: GalleryAdapter by viewLifecycleProperty()

    private val args: MediaViewerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaViewerAdapter = GalleryAdapter()

        setupViewPager()
        //setupSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            //postponeEnterTransition();
        }
    }

    private fun setupViewPager() = with(binding) {
        viewPager.adapter = mediaViewerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                galleryViewModel.sharedElementPosition = position
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    galleryViewModel.searchQueryResult.collectLatest {
                        val galleryData = it.map { mediaItem -> mediaItem.toGalleryListItem() }
                        mediaViewerAdapter.submitData(galleryData)
                    }
                }
            }
        }

        viewPager.run {
            post { setCurrentItem(0, false) }
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

}