package me.naloaty.photoprism.di.session_fragment

import com.yandex.yatagan.Component
import me.naloaty.photoprism.di.fragment.FragmentScope
import me.naloaty.photoprism.features.albums.presentation.AlbumsFragment
import me.naloaty.photoprism.features.gallery.presentation.GalleryFragment

@FragmentScope
@Component(isRoot = false)
interface SessionFragmentComponent {

    fun inject(fragment: GalleryFragment)
    fun inject(fragment: AlbumsFragment)

    @Component.Builder
    interface Builder {
        fun create(): SessionFragmentComponent
    }

}