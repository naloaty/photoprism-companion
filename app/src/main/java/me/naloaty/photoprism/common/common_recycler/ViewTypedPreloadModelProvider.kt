package me.naloaty.photoprism.common.common_recycler

import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.bumptech.glide.RequestBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped


abstract class ViewTypedPreloadModelProvider(
    private val adapter: BaseTiAdapter<*, *>
): PreloadModelProvider<ViewTyped> {

    abstract val factoriesRegistry: FactoriesRegistry

    override fun getPreloadItems(position: Int): List<ViewTyped> {
        val item = adapter.items[position]

        return if (factoriesRegistry.isPreloadable(item.javaClass)) {
            listOf(item)
        } else {
            emptyList()
        }
    }

    override fun getPreloadRequestBuilder(item: ViewTyped): RequestBuilder<*>? {
        val requestBuilderFactory = factoriesRegistry.getFactoryFor(item.javaClass) ?: return null
        return requestBuilderFactory.create(item)
    }

    fun interface RequestBuilderFactory<in T : ViewTyped> {

        fun create(item: T): RequestBuilder<*>
    }

    class FactoriesRegistry {

        companion object {
            inline operator fun invoke(block: FactoriesRegistry.() -> Unit): FactoriesRegistry {
                return FactoriesRegistry().apply(block)
            }
        }

        private val preloadFactories: MutableMap<Class<*>, RequestBuilderFactory<*>> = mutableMapOf()

        infix fun <T : ViewTyped> Class<T>.using(factory: RequestBuilderFactory<T>) {
            preloadFactories[this] = factory
        }

        inline fun <reified T : ViewTyped> requestFactory(factory: RequestBuilderFactory<T>) {
            T::class.java using factory
        }

        fun isPreloadable(type: Class<*>): Boolean {
            return type in preloadFactories
        }

        fun getFactoryFor(type: Class<*>): RequestBuilderFactory<ViewTyped>? {
            @Suppress("UNCHECKED_CAST")
            return preloadFactories[type] as? RequestBuilderFactory<ViewTyped>
        }
    }
}