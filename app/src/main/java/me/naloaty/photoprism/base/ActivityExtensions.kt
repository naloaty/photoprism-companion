package me.naloaty.photoprism.base

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel


inline fun <reified VM : ViewModel> BaseActivity.appViewModels() = viewModels<VM> {
    activityComponent.appComponent().viewModelFactory()
}



