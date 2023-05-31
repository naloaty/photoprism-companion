package me.naloaty.photoprism.api.exception


class UnexpectedBehaviorException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)