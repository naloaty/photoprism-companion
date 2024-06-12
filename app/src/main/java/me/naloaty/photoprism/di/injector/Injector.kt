package me.naloaty.photoprism.di.injector

import me.naloaty.photoprism.di.injector.Injector.ComponentKey.MultiComponentKey
import me.naloaty.photoprism.di.injector.Injector.ComponentKey.SingleComponentKey
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

object Injector {

    private val components = ConcurrentHashMap<ComponentKey<*>, Any>()

    fun <T : Any> getOrCreate(key: ComponentKey<T>, factory: () -> T): T {
        Timber.d("Get or create component = $key")
        @Suppress("UNCHECKED_CAST")
        return components.computeIfAbsent(key) {
            Timber.d("Component created for $key")
            factory()
        } as T
    }

    fun <T : Any> getOrCreate(klass: Class<T>, factory: () -> T): T {
        return getOrCreate(SingleComponentKey(klass), factory)
    }

    fun <T : Any> get(key: ComponentKey<T>): T {
        Timber.d("Get component = $key")

        val component = components[key]
            ?: throw RuntimeException("Component ${key.klass.canonicalName} does not exist yet")

        @Suppress("UNCHECKED_CAST")
        return component as T
    }

    fun <T : Any> get(klass: Class<T>): T {
        return get(SingleComponentKey(klass))
    }

    fun <T : Any> get(klass: Class<T>, tag: String): T {
        return get(MultiComponentKey(klass, tag))
    }

    fun <T : Any> clear(key: ComponentKey<T>) {
        components.remove(key)
        Timber.d("Clear component = $key")
    }

    sealed interface ComponentKey<T : Any> {
        val klass: Class<T>

        data class SingleComponentKey<T : Any>(
            override val klass: Class<T>
        ) : ComponentKey<T>
        data class MultiComponentKey<T : Any>(
            override val klass: Class<T>,
            val tag: String
        ) : ComponentKey<T>
    }
}