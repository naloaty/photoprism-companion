package me.naloaty.photoprism.features.common_paging.paginator

typealias RemotePageLoader<T> = suspend (offset: Int) -> List<T>