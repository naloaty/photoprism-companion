package me.naloaty.photoprism.di.session_flow_fragment.module

import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.common.LibraryUrlProvider
import me.naloaty.photoprism.common.SessionProvider
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.di.session_fragment.SessionFragmentComponent
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

@Module(
    subcomponents = [
        SessionFragmentComponent::class
    ]
)
object SessionModule {

    @[SessionFlowFragementScope Provides]
    fun provideLibraryUrlProvider(account: LibraryAccount): LibraryUrlProvider {
        return object : LibraryUrlProvider {
            override val libraryRoot: String = account.libraryRoot.trimEnd('/')
            override val libraryApiUrl: String = "$libraryRoot/api"
        }
    }


    @[SessionFlowFragementScope Provides]
    fun provideSessionProvider(session: LibraryAccountSession): SessionProvider {
        return object : SessionProvider {
            override val session: LibraryAccountSession = session
        }
    }
}