package me.naloaty.photoprism.di.app.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.naloaty.photoprism.AppViewModel
import me.naloaty.photoprism.AuthViewModel
import me.naloaty.photoprism.di.app.qualifier.AppViewModels
import me.naloaty.photoprism.di.flowfragment.qualifier.FlowFragmentViewModels
import me.naloaty.photoprism.di.viewmodel.ViewModelKey
import me.naloaty.photoprism.features.gallery.presentation.GalleryViewModel

@Module
interface AppViewModelModule {
    // WARNING: Don't scope view models!

    @[Binds ViewModelKey(AuthViewModel::class) IntoMap AppViewModels]
    fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @[Binds ViewModelKey(AppViewModel::class) IntoMap AppViewModels]
    fun bindAppViewModel(viewModel: AppViewModel): ViewModel


}