package me.naloaty.photoprism.common.common_paging.paginator

typealias NextPageFilterStrategy<T> = (current: List<T>, next: List<T>) -> List<T>

fun <T : Any> distinctBy(getKey: (T) -> Any): NextPageFilterStrategy<T> {
    return { current, next ->  
        next.filter { nextItem ->
            current.all { currentItem -> getKey(currentItem) != getKey(nextItem) }
        }
    }
}