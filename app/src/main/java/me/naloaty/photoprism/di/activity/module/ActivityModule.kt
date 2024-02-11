package me.naloaty.photoprism.di.activity.module

import android.content.Context
import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import me.naloaty.photoprism.base.BaseActivity
import me.naloaty.photoprism.di.activity.ActivityScope
import me.naloaty.photoprism.di.activity.qualifier.ActivityContext
import me.naloaty.photoprism.di.flow_fragment.FlowFragmentComponent
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragmentComponent

@Module(
    subcomponents = [
        FlowFragmentComponent::class,
        SessionFlowFragmentComponent::class
    ]
)
interface ActivityModule {

    @[ActivityScope Binds ActivityContext]
    fun provideContext(activity: BaseActivity): Context
}