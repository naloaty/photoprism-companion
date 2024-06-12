package me.naloaty.photoprism.common.common_viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner

interface SessionViewModelStoreOwner : ViewModelStoreOwner

val Fragment.sessionViewModelStoreOwner: ViewModelStoreOwner
    get() = when (this) {
        is SessionViewModelStoreOwner -> this
        else -> when (val parent = parentFragment) {
            null -> throw IllegalStateException("Session flow fragment is not found")
            else -> parent.sessionViewModelStoreOwner
        }
    }