package me.naloaty.photoprism.features.gallery_v2.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.databinding.FragmentGalleryBinding
import me.naloaty.photoprism.features.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.features.gallery.presentation.GalleryFragmentArgs
import me.naloaty.photoprism.features.gallery.presentation.GalleryFragmentDirections
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryNews
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryUiEvent.OnClickMediaItem
import me.naloaty.photoprism.navigation.navigateSafely
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.mobile.tech.ti_recycler.adapters.AsyncTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.diff.ViewTypedDiffCallback
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.TiRecyclerCoroutines
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory


private const val MIN_MEDIA_ITEMS_PER_ROW = 1
private const val GALLERY_CELL_SPAN = 1

class GalleryFragment : BaseSessionFragment(R.layout.fragment_gallery) {

    private val args: GalleryFragmentArgs by navArgs()
    private val binding: FragmentGalleryBinding by viewBinding()

    private val component by lazy { sessionFragmentComponent.galleryComponent() }
    private val store by storeViaViewModel { component.galleryStore }

    private val adapter = AsyncTiAdapter<ViewTyped, CoroutinesHolderFactory>(
        GalleryViewHolderFactory(), ViewTypedDiffCallback())

    private var tiRecycler: TiRecyclerCoroutines<ViewTyped> by viewLifecycleProperty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStore()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
    }

    private fun initStore() {
        store.collectOnCreate(
            fragment = this,
            uiStateMapper = component.uiStateMapper,
            stateCollector = ::collectState,
            newsCollector = ::collectNews
        )
    }

    private fun initRecyclerView() {
        tiRecycler = TiRecyclerCoroutines(binding.rvGallery, adapter) {
            layoutManager = createGridLayoutManager()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.holderFactory.clickPosition(R.layout.li_gallery_media_item)
                .collect { store.dispatch(OnClickMediaItem(it)) }
        }
    }

    private fun collectState(state: GalleryUiState) {
        tiRecycler.setItems(state.listItems)
    }

    private fun collectNews(news: GalleryNews) = when (news) {
        is GalleryNews.OpenPreview -> {
            val directions = GalleryFragmentDirections.actionViewMedia(news.position)
            findNavController().navigateSafely(directions)
        }
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        val itemsPerRow = calculateMediaItemsPerRow()

        return GridLayoutManager(context, itemsPerRow).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (adapter.getItemViewType(position)) {
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
        return (rowWidthPx / minItemWidthPx).coerceAtLeast(MIN_MEDIA_ITEMS_PER_ROW)
    }

}