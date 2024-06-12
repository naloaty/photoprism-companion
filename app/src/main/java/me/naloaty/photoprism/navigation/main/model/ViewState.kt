package me.naloaty.photoprism.navigation.main.model

import androidx.annotation.AnimRes

sealed interface ViewState {

    val animResId: Int?

    data class Shown(@AnimRes override val animResId: Int?): ViewState
    data class Hidden(@AnimRes override val animResId: Int?): ViewState
}