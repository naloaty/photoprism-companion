package me.naloaty.photoprism.di.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.naloaty.photoprism.di.session.qualifier.SessionViewModels
import javax.inject.Inject
import javax.inject.Provider

@SessionScope
class SessionViewModelFactory @Inject constructor(
    @SessionViewModels
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = providers[modelClass]
            ?: throw RuntimeException("Unknown ViewModel class '$modelClass'. Make sure you provided it correctly.")

        return try {
            @Suppress("UNCHECKED_CAST")
            provider.get() as T
        } catch (e: Exception) {
            throw RuntimeException("Could not cast provided ViewModel to class '$modelClass'", e)
        }
    }
}