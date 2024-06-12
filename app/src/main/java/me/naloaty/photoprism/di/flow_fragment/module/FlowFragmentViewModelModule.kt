package me.naloaty.photoprism.di.flow_fragment.module

import androidx.lifecycle.ViewModel
import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoMap
import com.yandex.yatagan.Module
import me.naloaty.photoprism.di.flow_fragment.qualifier.FlowFragmentViewModels
import me.naloaty.photoprism.di.viewmodel.ViewModelKey
import me.naloaty.photoprism.features.auth.presentation.LibraryConnectViewModel

@Module(includes = [FlowFragmentViewModelModule.Bindings::class])
object FlowFragmentViewModelModule {

    @Module
    interface Bindings {

        @[Binds ViewModelKey(LibraryConnectViewModel::class) IntoMap FlowFragmentViewModels]
        fun bindLibraryConnectViewModel(viewModel: LibraryConnectViewModel): ViewModel
    }

}