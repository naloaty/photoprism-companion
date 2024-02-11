package me.naloaty.photoprism.di.session_flow_fragment.module

import androidx.lifecycle.ViewModel
import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoMap
import com.yandex.yatagan.Module
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.SessionViewModels
import me.naloaty.photoprism.di.viewmodel.ViewModelKey
import me.naloaty.photoprism.features.albums.presentation.AlbumsViewModel
import me.naloaty.photoprism.features.gallery.presentation.GalleryViewModel
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewModel

@Module
interface SessionViewModelModule {
    // WARNING: Don't scope view models!

    @[Binds ViewModelKey(GalleryViewModel::class) IntoMap SessionViewModels]
    fun bindGalleryViewModel(viewModel: GalleryViewModel): ViewModel

    @[Binds ViewModelKey(AlbumsViewModel::class) IntoMap SessionViewModels]
    fun bindAlbumsViewModel(viewModel: AlbumsViewModel): ViewModel

    @[Binds ViewModelKey(MediaViewModel::class) IntoMap SessionViewModels]
    fun bindMediaViewerViewModel(viewModel: MediaViewModel): ViewModel

}