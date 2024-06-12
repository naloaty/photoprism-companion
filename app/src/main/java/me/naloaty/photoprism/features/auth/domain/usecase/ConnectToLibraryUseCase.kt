package me.naloaty.photoprism.features.auth.domain.usecase

import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.PhotoPrismAuthenticator
import me.naloaty.photoprism.features.auth.data.mapper.toLibraryAccountSession
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import me.naloaty.photoprism.features.auth.domain.model.LibraryConnectParams
import me.naloaty.photoprism.util.toApiUrl
import javax.inject.Inject

class ConnectToLibraryUseCase @Inject constructor(
    private val authenticator: PhotoPrismAuthenticator
) {

    suspend operator fun invoke(connectParams: LibraryConnectParams): LibraryAccountSession {
        val session = when(connectParams) {
            is LibraryConnectParams.Public -> {
                authenticator.logIn(
                    apiUrl = connectParams.libraryRoot.toApiUrl(),
                    username = "",
                    password = ""
                )
            }

            is LibraryConnectParams.Private -> {
                authenticator.logIn(
                    apiUrl = connectParams.libraryRoot.toApiUrl(),
                    username = connectParams.username,
                    password = connectParams.password
                )
            }
        }

        return session.toLibraryAccountSession()
    }

}