package me.naloaty.photoprism.di.activity

import com.yandex.yatagan.Component
import me.naloaty.photoprism.di.activity.module.ActivityModule
import me.naloaty.photoprism.di.app.AppComponent
import me.naloaty.photoprism.di.flow_fragment.FlowFragmentComponent
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragmentComponent


@ActivityScope
@Component(
    isRoot = false,
    modules = [
        ActivityModule::class
    ]
)
interface ActivityComponent {

    fun appComponent(): AppComponent
    fun flowFragmentComponentFactory(): FlowFragmentComponent.Builder
    fun sessionFlowFragmentComponentFactory(): SessionFlowFragmentComponent.Builder

    @Component.Builder
    interface Builder {
        fun create(): ActivityComponent
    }

}