package me.naloaty.photoprism.di.fragment

import dagger.Subcomponent
import me.naloaty.photoprism.features.auth.presentation.LibraryConnectFragment

@FragmentScope
@Subcomponent
interface FragmentComponent {

    fun inject(fragment: LibraryConnectFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }
}