package me.naloaty.photoprism.common.common_ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import me.naloaty.photoprism.di.injector.get
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> Fragment.viewLifecycleProperty(): ReadWriteProperty<Any, T> {
    return ViewLifecycleProperty { viewLifecycleOwner.lifecycle }
}

inline fun <reified T: Any> propertyViaViewModel(
    noinline defaultValueProvider: () -> T
): ReadWriteProperty<ViewModelStoreOwner, T> {
    return ViewModelProperty(defaultValueProvider)
}


private class ViewLifecycleProperty<V>(
    private val lifecycleProvider: () -> Lifecycle
) : ReadWriteProperty<Any, V>, DefaultLifecycleObserver {

    private var propertyValue: V? = null

    override operator fun getValue(thisRef: Any, property: KProperty<*>): V {
        return checkNotNull(propertyValue) {
            "Property is not initialized"
        }
    }

    override operator fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
        check(lifecycleProvider().currentState != Lifecycle.State.DESTROYED) {
            "Could not assign value in DESTROYED state"
        }

        propertyValue = value
        lifecycleProvider().addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        propertyValue = null
        owner.lifecycle.removeObserver(this)
    }
}

class ViewModelProperty<T : Any>(
    private val defaultValueProvider: () -> T
): ReadWriteProperty<ViewModelStoreOwner, T> {

    private var cache: T? = null

    override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
        return cache ?: run {
            val vm = getViewModel(thisRef, property)
            vm.value.also { cache = it }
        }
    }

    override fun setValue(thisRef: ViewModelStoreOwner, property: KProperty<*>, value: T) {
        val vm = getViewModel(thisRef, property)
        vm.value = value
        cache = value
    }

    private fun getViewModel(thisRef: ViewModelStoreOwner, property: KProperty<*>): PropertyViewModel<T> {
        return thisRef.viewModelStore.get(keyFromProperty(thisRef, property)) {
            PropertyViewModel(defaultValueProvider())
        }
    }

    private fun keyFromProperty(thisRef: ViewModelStoreOwner, property: KProperty<*>): String {
        return thisRef::class.java.canonicalName!! + "#" + property.name
    }

}

private class PropertyViewModel<T>(var value: T) : ViewModel()