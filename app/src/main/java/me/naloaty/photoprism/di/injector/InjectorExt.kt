package me.naloaty.photoprism.di.injector

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import me.naloaty.photoprism.di.injector.Injector.ComponentKey.MultiComponentKey
import me.naloaty.photoprism.di.injector.Injector.ComponentKey.SingleComponentKey
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragmentComponent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : Any> LifecycleOwner.componentUser(
    noinline componentTag: (() -> String)? = null,
): ReadOnlyProperty<LifecycleOwner, T> {
    return ComponentUser(
        componentClass = T::class.java,
        componentTag = componentTag,
        lifecycleProvider = { lifecycle }
    )
}

//inline fun <reified T : Any> LifecycleOwner.componentOwner(
//    noinline componentTag: (() -> String)? = null,
//    noinline componentFactory: () -> T
//): ReadOnlyProperty<LifecycleOwner, T> {
//    return ComponentOwner(
//        componentClass = T::class.java,
//        componentTag = componentTag,
//        componentFactory = componentFactory,
//        lifecycleProvider = { lifecycle }
//    )
//}
//
//inline fun <reified T : Any> LifecycleOwner.sessionComponentOwner(
//    noinline componentTag: (() -> String)? = null,
//    noinline componentFactory: SessionFlowFragmentComponent.() -> T
//): ReadOnlyProperty<LifecycleOwner, T> {
//    return ComponentOwner(
//        componentClass = T::class.java,
//        componentTag = componentTag,
//        componentFactory = {
//            val sessionComponent = Injector.get(SessionFlowFragmentComponent::class.java)
//            sessionComponent.componentFactory()
//        },
//        lifecycleProvider = { lifecycle }
//    )
//}

inline fun <reified T : Any> componentOwner(
    noinline componentTag: (() -> String)? = null,
    noinline componentFactory: () -> T
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return ComponentOwnerVm(
        componentClass = T::class.java,
        componentTag = componentTag,
        componentFactory = componentFactory
    )
}

inline fun <reified T : Any> sessionComponentOwner(
    noinline componentTag: (() -> String)? = null,
    noinline componentFactory: SessionFlowFragmentComponent.() -> T
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return ComponentOwnerVm(
        componentClass = T::class.java,
        componentTag = componentTag,
        componentFactory = {
            val sessionComponent = Injector.get(SessionFlowFragmentComponent::class.java)
            sessionComponent.componentFactory()
        },
    )
}

class ComponentOwner<out T : Any> (
    componentClass: Class<T>,
    componentTag: (() -> String)?,
    private val componentFactory: () -> T,
    private val lifecycleProvider: () -> Lifecycle
) : ReadOnlyProperty<LifecycleOwner, T>, DefaultLifecycleObserver {

    private var cache: T? = null

    private val key by lazy {
        if (componentTag == null) {
            SingleComponentKey(componentClass)
        } else {
            MultiComponentKey(componentClass, componentTag())
        }
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        lifecycleProvider().addObserver(this)
        return cache ?: Injector.getOrCreate(key, componentFactory).also { cache = it }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cache = null
        Injector.clear(key)
        owner.lifecycle.removeObserver(this)
    }
}

class ComponentOwnerVm<out T : Any> (
    componentClass: Class<T>,
    componentTag: (() -> String)?,
    private val componentFactory: () -> T,
) : ReadOnlyProperty<ViewModelStoreOwner, T> {

    private var cache: T? = null

    private val key by lazy {
        if (componentTag == null) {
            SingleComponentKey(componentClass)
        } else {
            MultiComponentKey(componentClass, componentTag())
        }
    }

    override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
        return cache ?: run {
            val vm = thisRef.viewModelStore.get(key.toString()) {
                ValueViewModel(
                    Injector.getOrCreate(key, componentFactory)
                ) {
                    cache = null
                    Injector.clear(key)
                }
            }
            vm.value.also { cache = it }
        }
    }
}

class ComponentUser<out T : Any> (
    componentClass: Class<T>,
    componentTag: (() -> String)?,
    private val lifecycleProvider: () -> Lifecycle
) : ReadOnlyProperty<LifecycleOwner, T>, DefaultLifecycleObserver {

    private var cache: T? = null

    private val key by lazy {
        if (componentTag == null) {
            SingleComponentKey(componentClass)
        } else {
            MultiComponentKey(componentClass, componentTag())
        }
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        lifecycleProvider().addObserver(this)
        return cache ?: Injector.get(key).also { cache = it }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cache = null
        owner.lifecycle.removeObserver(this)
    }
}

private class ValueViewModel<T>(
    val value: T,
    val onClear: () -> Unit = {}
) : ViewModel() {

    override fun onCleared() {
        onClear()
    }
}

inline fun <reified T : ViewModel> ViewModelStore.get(key: String, crossinline factory: () -> T): T {
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = factory() as VM
    }
    return ViewModelProvider(this, viewModelFactory).get(key, T::class.java)
}
