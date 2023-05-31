package me.naloaty.photoprism.di.session

import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineExceptionHandler
import me.naloaty.photoprism.di.session.module.AlbumsModule
import me.naloaty.photoprism.di.session.module.GalleryModule
import me.naloaty.photoprism.di.session.module.UrlFactoryModule
import me.naloaty.photoprism.di.fragmentwithsession.FragmentWithSessionComponent
import me.naloaty.photoprism.di.session.module.NetworkModule
import me.naloaty.photoprism.di.session.module.SessionModule
import me.naloaty.photoprism.di.session.module.SessionViewModelModule
import me.naloaty.photoprism.features.auth.domain.AuthExceptionHandler
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

@SessionScope
@Subcomponent(
    modules = [
        SessionModule::class,
        NetworkModule::class,
        SessionViewModelModule::class,
        GalleryModule::class,
        AlbumsModule::class,
        UrlFactoryModule::class
    ]
)
interface SessionComponent {

    fun viewModelFactory(): SessionViewModelFactory
    fun fragmentWithSessionComponentFactory(): FragmentWithSessionComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance account: LibraryAccount,
            @BindsInstance session: LibraryAccountSession,
        ): SessionComponent
    }

}