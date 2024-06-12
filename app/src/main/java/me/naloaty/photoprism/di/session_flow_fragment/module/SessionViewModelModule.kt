package me.naloaty.photoprism.di.session_flow_fragment.module

import androidx.lifecycle.ViewModel
import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoMap
import com.yandex.yatagan.Module
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.SessionViewModels
import me.naloaty.photoprism.di.viewmodel.ViewModelKey
import me.naloaty.photoprism.features.media_viewer.ui.VideoPlayerCacheViewModel

@Module(includes = [SessionViewModelModule.Bindings::class])
object SessionViewModelModule {

    @Module
    interface Bindings {

        @[Binds ViewModelKey(VideoPlayerCacheViewModel::class) IntoMap SessionViewModels]
        fun bindVideoPlayerCacheViewModel(viewModel: VideoPlayerCacheViewModel): ViewModel
    }

}