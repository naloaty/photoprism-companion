package me.naloaty.photoprism.di.flowfragment.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.naloaty.photoprism.di.flowfragment.qualifier.FlowFragmentViewModels
import me.naloaty.photoprism.di.viewmodel.ViewModelKey
import me.naloaty.photoprism.features.auth.presentation.LibraryConnectViewModel
import me.naloaty.photoprism.AuthViewModel

@Module
interface FlowFragmentViewModelModule {
    // WARNING: Don't scope view models!

    @[Binds ViewModelKey(LibraryConnectViewModel::class) IntoMap FlowFragmentViewModels]
    fun bindLibraryConnectViewModel(viewModel: LibraryConnectViewModel): ViewModel

}