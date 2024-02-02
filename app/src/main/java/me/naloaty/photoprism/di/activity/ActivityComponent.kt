package me.naloaty.photoprism.di.activity

import com.yandex.yatagan.BindsInstance
import com.yandex.yatagan.Component
import me.naloaty.photoprism.AppActivity
import me.naloaty.photoprism.base.BaseActivity
import me.naloaty.photoprism.di.activity.module.ActivityModule
import me.naloaty.photoprism.di.app.AppComponent
import me.naloaty.photoprism.di.flowfragment.FlowFragmentComponent
import me.naloaty.photoprism.di.session.SessionComponent


@ActivityScope
@Component(
    isRoot = false,
    modules = [
        ActivityModule::class
    ]
)
interface ActivityComponent {

    fun appComponent(): AppComponent
    fun flowFragmentComponentFactory(): FlowFragmentComponent.Builder
    fun sessionComponentFactory(): SessionComponent.Builder

    fun inject(activity: AppActivity)

    @Component.Builder
    interface Builder {
        fun create(
            @BindsInstance activity: BaseActivity
        ): ActivityComponent
    }

}