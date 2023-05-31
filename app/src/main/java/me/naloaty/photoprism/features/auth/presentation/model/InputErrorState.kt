package me.naloaty.photoprism.features.auth.presentation.model

import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

sealed interface InputErrorState {

    object None : InputErrorState
    data class Error(@StringRes val messageResId: Int? = null): InputErrorState
}


fun TextInputLayout.bindToInputErrorState(state: InputErrorState) =
    when(state) {
        is InputErrorState.None -> {
            this.error = null
        }

        is InputErrorState.Error -> {
            if (state.messageResId != null) {
                this.error = resources.getString(state.messageResId)
            } else {
                this.error = " "
            }
        }
    }