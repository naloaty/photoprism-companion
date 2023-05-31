package me.naloaty.photoprism.di.service

import dagger.BindsInstance
import dagger.Subcomponent
import me.naloaty.photoprism.base.BaseService
import me.naloaty.photoprism.di.service.module.ServiceModule
import me.naloaty.photoprism.features.auth.platform.AuthenticatorService

@ServiceScope
@Subcomponent(
    modules = [
        ServiceModule::class
    ]
)
interface ServiceComponent {

    fun inject(service: AuthenticatorService)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance service: BaseService
        ): ServiceComponent
    }
}