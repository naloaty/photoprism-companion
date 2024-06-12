package me.naloaty.photoprism.di.service

import com.yandex.yatagan.BindsInstance
import com.yandex.yatagan.Component
import me.naloaty.photoprism.base.BaseService
import me.naloaty.photoprism.di.service.module.ServiceModule
import me.naloaty.photoprism.features.auth.platform.AuthenticatorService

@ServiceScope
@Component(
    isRoot = false,
    modules = [
        ServiceModule::class
    ]
)
interface ServiceComponent {

    fun inject(service: AuthenticatorService)

    @Component.Builder
    interface Builder {
        fun create(
            @BindsInstance service: BaseService
        ): ServiceComponent
    }
}