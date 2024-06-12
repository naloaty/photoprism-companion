package me.naloaty.photoprism.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import me.naloaty.photoprism.common.common_viewmodel.sessionViewModelStoreOwner
import me.naloaty.photoprism.di.injector.Injector
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragmentComponent


inline fun <reified VM : ViewModel> Fragment.sessionFragmentViewModel() = viewModels<VM> {
    val sessionComponent = Injector.get(SessionFlowFragmentComponent::class.java)
    sessionComponent.viewModelFactory()
}

inline fun <reified VM : ViewModel> Fragment.sessionFlowFragmentViewModel() = viewModels<VM>(
    ownerProducer = { sessionViewModelStoreOwner }
) {
    val sessionComponent = Injector.get(SessionFlowFragmentComponent::class.java)
    sessionComponent.viewModelFactory()
}