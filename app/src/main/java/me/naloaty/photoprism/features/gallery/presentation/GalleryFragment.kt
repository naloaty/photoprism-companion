package me.naloaty.photoprism.features.gallery.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.flowFragmentViewModel
import me.naloaty.photoprism.base.sessionViewModels
import me.naloaty.photoprism.features.common_ext.viewLifecycleProperty
import me.naloaty.photoprism.databinding.FragmentGalleryBinding
import me.naloaty.photoprism.navigation.main.BottomNavViewModel


class GalleryFragment : BaseSessionFragment(R.layout.fragment_gallery) {

    private val galleryViewModel: GalleryViewModel by sessionViewModels()
    private val bottomNavViewModel: BottomNavViewModel by flowFragmentViewModel()

    private val viewBinding: FragmentGalleryBinding by viewBinding()
    private val args: GalleryFragmentArgs by navArgs()

    private var galleryRenderer: GalleryRenderer by viewLifecycleProperty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryRenderer = GalleryRenderer(
            requireContext(),
            viewBinding,
            viewLifecycleOwner,
            galleryViewModel,
            bottomNavViewModel
        )

        galleryRenderer.init(args.albumUid)
    }

}