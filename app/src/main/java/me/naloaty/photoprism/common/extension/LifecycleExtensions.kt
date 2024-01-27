package me.naloaty.photoprism.common.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


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


fun <T> Fragment.viewLifecycleProperty(): ReadWriteProperty<Any, T> {
    return ViewLifecycleProperty { viewLifecycleOwner.lifecycle }
}