package me.naloaty.photoprism.features.auth.domain.exception

import java.io.IOException


open class AuthException(
    message: String? = null,
    cause: Throwable? = null
) : IOException(message, cause)