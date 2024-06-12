package me.naloaty.photoprism.base

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import me.naloaty.photoprism.di.app.AppComponent
import me.naloaty.photoprism.di.injector.Injector


inline fun <reified VM : ViewModel> ComponentActivity.appViewModels() = viewModels<VM> {
    Injector.get(AppComponent::class.java).viewModelFactory()
}



