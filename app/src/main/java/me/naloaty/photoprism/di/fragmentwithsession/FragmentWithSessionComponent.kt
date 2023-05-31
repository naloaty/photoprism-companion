package me.naloaty.photoprism.di.fragmentwithsession

import dagger.Subcomponent
import me.naloaty.photoprism.di.fragment.FragmentScope
import me.naloaty.photoprism.features.albums.presentation.AlbumsFragment
import me.naloaty.photoprism.features.gallery.presentation.GalleryFragment

@FragmentScope
@Subcomponent
interface FragmentWithSessionComponent {

    fun inject(fragment: GalleryFragment)
    fun inject(fragment: AlbumsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentWithSessionComponent
    }

}