package me.naloaty.photoprism.base

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import me.naloaty.photoprism.navigation.main.MainFlowFragment


inline fun <reified VM : ViewModel> BaseSessionFragment.sessionFragmentViewModel() = viewModels<VM> {
    val flowFragment = requireParentFragment().requireParentFragment() as MainFlowFragment
    flowFragment.sessionComponent.viewModelFactory()
}

inline fun <reified VM : ViewModel> BaseSessionFragment.sessionFlowFragmentViewModel() = viewModels<VM>(
    ownerProducer = { requireParentFragment().requireParentFragment() }
) {
    val flowFragment = requireParentFragment().requireParentFragment() as MainFlowFragment
    flowFragment.sessionComponent.viewModelFactory()
}