package me.naloaty.photoprism.features.auth.domain

import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.features.auth.domain.exception.InvalidSessionException
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import me.naloaty.photoprism.features.auth.domain.usecase.InvalidateSessionUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@SessionFlowFragementScope
class PhotoPrismAuthInterceptor @Inject constructor(
    private val handler: AuthExceptionHandler,
    private val session: LibraryAccountSession,
    private val invalidateSessionUseCase: InvalidateSessionUseCase,
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader("X-Session-ID", session.sessionId)

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            invalidateSessionUseCase(session)

            InvalidSessionException().also {
                handler.notify(it)
                response.close()
                throw it
            }
        } else {
            return response
        }
    }

}