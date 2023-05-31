package me.naloaty.photoprism.di.flowfragment

import dagger.BindsInstance
import dagger.Subcomponent
import me.naloaty.photoprism.base.BaseFlowFragment
import me.naloaty.photoprism.di.flowfragment.module.FlowFragmentModule
import me.naloaty.photoprism.di.flowfragment.module.FlowFragmentViewModelModule
import me.naloaty.photoprism.di.fragment.FragmentComponent
import me.naloaty.photoprism.navigation.main.MainFlowFragment

@FlowFragmentScope
@Subcomponent(
    modules = [
        FlowFragmentModule::class,
        FlowFragmentViewModelModule::class
    ]
)
interface FlowFragmentComponent {

    fun viewModelFactory(): FlowFragmentViewModelFactory
    fun fragmentComponentFactory(): FragmentComponent.Factory

    fun inject(fragment: MainFlowFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: BaseFlowFragment
        ): FlowFragmentComponent
    }
}