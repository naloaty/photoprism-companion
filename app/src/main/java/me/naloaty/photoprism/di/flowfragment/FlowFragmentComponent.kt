package me.naloaty.photoprism.di.flowfragment

import com.yandex.yatagan.BindsInstance
import com.yandex.yatagan.Component
import me.naloaty.photoprism.base.BaseFlowFragment
import me.naloaty.photoprism.di.flowfragment.module.FlowFragmentModule
import me.naloaty.photoprism.di.flowfragment.module.FlowFragmentViewModelModule
import me.naloaty.photoprism.di.fragment.FragmentComponent
import me.naloaty.photoprism.navigation.main.MainFlowFragment

@FlowFragmentScope
@Component(
    isRoot = false,
    modules = [
        FlowFragmentModule::class,
        FlowFragmentViewModelModule::class
    ]
)
interface FlowFragmentComponent {

    fun viewModelFactory(): FlowFragmentViewModelFactory
    fun fragmentComponentFactory(): FragmentComponent.Builder

    fun inject(fragment: MainFlowFragment)

    @Component.Builder
    interface Builder {
        fun create(
            @BindsInstance fragment: BaseFlowFragment
        ): FlowFragmentComponent
    }
}