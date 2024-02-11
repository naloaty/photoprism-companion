package me.naloaty.photoprism.di.session_flow_fragment

import com.yandex.yatagan.BindsInstance
import com.yandex.yatagan.Component
import me.naloaty.photoprism.di.session_flow_fragment.module.AlbumsModule
import me.naloaty.photoprism.di.session_flow_fragment.module.GalleryModule
import me.naloaty.photoprism.di.session_flow_fragment.module.NetworkModule
import me.naloaty.photoprism.di.session_flow_fragment.module.SessionModule
import me.naloaty.photoprism.di.session_flow_fragment.module.SessionViewModelModule
import me.naloaty.photoprism.di.session_flow_fragment.module.UrlFactoryModule
import me.naloaty.photoprism.di.session_fragment.SessionFragmentComponent
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

@SessionFlowFragementScope
@Component(
    isRoot = false,
    modules = [
        SessionModule::class,
        NetworkModule::class,
        SessionViewModelModule::class,
        GalleryModule::class,
        AlbumsModule::class,
        UrlFactoryModule::class
    ]
)
interface SessionFlowFragmentComponent {

    fun viewModelFactory(): SessionFlowFragmentViewModelFactory
    fun sessionFragmentComponentFactory(): SessionFragmentComponent.Builder

    @Component.Builder
    interface Builder {
        fun create(
            @BindsInstance account: LibraryAccount,
            @BindsInstance session: LibraryAccountSession,
        ): SessionFlowFragmentComponent
    }

}