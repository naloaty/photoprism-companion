package me.naloaty.photoprism.di.fragment

import com.yandex.yatagan.Component
import me.naloaty.photoprism.features.auth.presentation.LibraryConnectFragment

@FragmentScope
@Component(isRoot = false)
interface FragmentComponent {

    fun inject(fragment: LibraryConnectFragment)

    @Component.Builder
    interface Builder {
        fun create(): FragmentComponent
    }
}