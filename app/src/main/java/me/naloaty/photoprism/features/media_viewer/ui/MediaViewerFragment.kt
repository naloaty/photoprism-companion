package me.naloaty.photoprism.features.media_viewer.ui

import androidx.navigation.fragment.navArgs
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.sessionViewModels
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerViewModel

class MediaViewerFragment: BaseSessionFragment(R.layout.fragment_media_viewer) {

    private val viewModel: MediaViewerViewModel by sessionViewModels()

    private val args: MediaViewerFragmentArgs by navArgs()

}