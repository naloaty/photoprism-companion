package me.naloaty.photoprism.common.common_paging.paginator

typealias RemotePageLoader<T> = suspend (offset: Int) -> List<T>