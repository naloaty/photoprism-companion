package me.naloaty.photoprism.di.session_flow_fragment.module

import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.di.session_fragment.SessionFragmentComponent
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.ApiUrl
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount

@Module(
    subcomponents = [
        SessionFragmentComponent::class
    ]
)
interface SessionModule {

    companion object {

        @[SessionFlowFragementScope Provides ApiUrl]
        fun provideApiUrl(account: LibraryAccount) =
            account.libraryRoot.trimEnd('/') + "/api"
    }
}