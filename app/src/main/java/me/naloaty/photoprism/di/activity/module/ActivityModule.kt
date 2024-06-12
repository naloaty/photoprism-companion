package me.naloaty.photoprism.di.activity.module

import com.yandex.yatagan.Module
import me.naloaty.photoprism.di.flow_fragment.FlowFragmentComponent
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragmentComponent

@Module(
    subcomponents = [
        FlowFragmentComponent::class,
        SessionFlowFragmentComponent::class
    ]
)
object ActivityModule