package me.naloaty.photoprism.base

import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.kotea.core.Store
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <T : Store<*, *, *>> storeViaViewModel(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    crossinline ownerProducer: () -> ViewModelStoreOwner? = { null },
    crossinline sharedViewModelKey: () -> String? = { null },
    noinline factory: () -> T
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return object : ReadOnlyProperty<ViewModelStoreOwner, T> {
        private val owner by lazy(LazyThreadSafetyMode.NONE) { ownerProducer() }

        private val delegate by lazy(LazyThreadSafetyMode.NONE) {
            storeViaViewModel(coroutineContext, sharedViewModelKey(), factory)
        }

        override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
            return delegate.getValue(owner ?: thisRef, property)
        }
    }
}