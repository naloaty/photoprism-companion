package me.naloaty.photoprism.di.flowfragment.module

import dagger.Module
import me.naloaty.photoprism.di.fragment.FragmentComponent

@Module(
    subcomponents = [
        FragmentComponent::class
    ]
)
interface FlowFragmentModule