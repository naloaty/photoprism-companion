package me.naloaty.photoprism.di.session_fragment

import com.yandex.yatagan.Component
import me.naloaty.photoprism.di.fragment.FragmentScope

@FragmentScope
@Component(isRoot = false)
interface SessionFragmentComponent {

    @Component.Builder
    interface Builder {
        fun create(): SessionFragmentComponent
    }

}