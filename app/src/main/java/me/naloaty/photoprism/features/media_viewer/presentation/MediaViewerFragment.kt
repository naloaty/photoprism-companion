package me.naloaty.photoprism.features.media_viewer.presentation

import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseSessionFragment
import me.naloaty.photoprism.base.sessionViewModels

class MediaViewerFragment: BaseSessionFragment(R.layout.fragment_media_viewer) {

    private val viewModel: MediaViewerViewModel by sessionViewModels()

}