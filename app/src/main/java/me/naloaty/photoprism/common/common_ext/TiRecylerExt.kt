@file:Suppress("UNCHECKED_CAST")

package me.naloaty.photoprism.common.common_ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory
import kotlin.reflect.KClass

fun <T : ViewTyped> BaseTiAdapter<ViewTyped, CoroutinesHolderFactory>.clickedItem(
    vararg viewType: Int
): Flow<T> {
    return holderFactory.clickPosition(*viewType).map { items[it] as T }
}

fun <T : ViewTyped> BaseTiAdapter<ViewTyped, CoroutinesHolderFactory>.clickedViewId(
    viewType: Int, viewId: Int
): Flow<T> {
    return holderFactory.clickPosition(viewType, viewId).map { items[it] as T }
}

fun <T : ViewTyped> BaseTiAdapter<ViewTyped, CoroutinesHolderFactory>.longClickedItem(
    vararg viewType: Int
): Flow<T> {
    return holderFactory.longClickPosition(*viewType).map { items[it] as T }
}

fun <T : ViewTyped> BaseTiAdapter<ViewTyped, CoroutinesHolderFactory>.longClickedViewId(
    viewType: Int, viewId: Int
): Flow<T> {
    return holderFactory.longClickPosition(viewType, viewId)
        .map { items[it] as T }
}

fun <T : ViewTyped> BaseTiAdapter<ViewTyped, CoroutinesHolderFactory>.checkChanged(
    viewType: Int,
    viewId: Int
): Flow<Pair<T, Boolean>> {
    return holderFactory.checkChanges(viewType, viewId)
        .map { (clickedPosition, isChecked) -> items[clickedPosition] as T to isChecked }
}

fun <T : ViewTyped> BaseTiAdapter<ViewTyped, CoroutinesHolderFactory>.swipeToDismiss(
    vararg viewType: Int
): Flow<T> {
    return holderFactory.swipesToDismiss(*viewType)
        .map { items[it] as T }
}

fun <T : ViewTyped, A : Any> BaseTiAdapter<ViewTyped, CoroutinesHolderFactory>.customAction(
    viewType: Int,
    actionClass: KClass<A>,
): Flow<Pair<T, A>> {
    return holderFactory.customAction(viewType, actionClass)
        .map { (position, value) -> items[position] as T to value }
}