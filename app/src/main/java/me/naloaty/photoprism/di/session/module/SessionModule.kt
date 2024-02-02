package me.naloaty.photoprism.di.session.module

import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.di.fragmentwithsession.FragmentWithSessionComponent
import me.naloaty.photoprism.di.session.SessionScope
import me.naloaty.photoprism.di.session.qualifier.ApiUrl
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount

@Module(
    subcomponents = [
        FragmentWithSessionComponent::class
    ]
)
interface SessionModule {

    companion object {

        @[SessionScope Provides ApiUrl]
        fun provideApiUrl(account: LibraryAccount) =
            account.libraryRoot.trimEnd('/') + "/api"
    }
}