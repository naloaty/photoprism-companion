package me.naloaty.photoprism.di.app.module

import androidx.lifecycle.ViewModel
import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoMap
import com.yandex.yatagan.Module
import me.naloaty.photoprism.AppViewModel
import me.naloaty.photoprism.AuthViewModel
import me.naloaty.photoprism.di.app.qualifier.AppViewModels
import me.naloaty.photoprism.di.viewmodel.ViewModelKey

@Module(includes = [AppViewModelModule.Bindings::class])
object AppViewModelModule {

    @Module
    interface Bindings {

        @[Binds ViewModelKey(AuthViewModel::class) IntoMap AppViewModels]
        fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

        @[Binds ViewModelKey(AppViewModel::class) IntoMap AppViewModels]
        fun bindAppViewModel(viewModel: AppViewModel): ViewModel
    }
}