package me.naloaty.photoprism.base

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import me.naloaty.photoprism.di.fragmentwithsession.FragmentWithSessionComponent
import me.naloaty.photoprism.navigation.main.MainFlowFragment

inline fun <reified VM : ViewModel> BaseSessionFragment.sessionViewModels() = viewModels<VM> {
    val flowFragment = requireParentFragment().requireParentFragment() as MainFlowFragment
    flowFragment.sessionComponent.viewModelFactory()
}


inline fun <reified VM : ViewModel> BaseSessionFragment.flowFragmentViewModel() = viewModels<VM>(
    ownerProducer = { requireParentFragment().requireParentFragment() }
)