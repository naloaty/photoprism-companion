package me.naloaty.photoprism.common.common_search

import me.naloaty.photoprism.common.mvvm.Event

sealed interface SearchEffect {
    class HideSearchView : SearchEffect, Event<Unit>(Unit)
    data class UpdateSearchText(val value: String) : SearchEffect
}