package me.naloaty.photoprism.features.media_viewer.di

import com.yandex.yatagan.Component
import me.naloaty.photoprism.features.media_viewer.ui.mapper.PagerUiStateMapper

@Component(
    isRoot = false
)
interface MediaViewerComponent {

    val pagerUiStateMapper: PagerUiStateMapper
}