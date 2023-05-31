package me.naloaty.photoprism.di.activity

import dagger.BindsInstance
import dagger.Subcomponent
import me.naloaty.photoprism.AppActivity
import me.naloaty.photoprism.base.BaseActivity
import me.naloaty.photoprism.di.activity.module.ActivityModule
import me.naloaty.photoprism.di.app.AppComponent
import me.naloaty.photoprism.di.flowfragment.FlowFragmentComponent
import me.naloaty.photoprism.di.session.SessionComponent


@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class
    ]
)
interface ActivityComponent {

    fun appComponent(): AppComponent
    fun flowFragmentComponentFactory(): FlowFragmentComponent.Factory
    fun sessionComponentFactory(): SessionComponent.Factory

    fun inject(activity: AppActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: BaseActivity
        ): ActivityComponent
    }

}