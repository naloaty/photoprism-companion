package me.naloaty.photoprism.navigation.main.model

import androidx.annotation.AnimRes

sealed class ViewState(
    @AnimRes val animResId: Int?
) {

    class Shown(@AnimRes animResId: Int?): ViewState(animResId)
    class Hidden(@AnimRes animResId: Int?): ViewState(animResId)
}