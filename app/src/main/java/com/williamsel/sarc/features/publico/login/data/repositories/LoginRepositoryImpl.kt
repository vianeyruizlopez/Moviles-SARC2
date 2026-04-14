package com.williamsel.sarc.features.publico.login.data.repositories

import com.williamsel.sarc.features.publico.login.data.datasource.GoogleAuthUiClient
import com.williamsel.sarc.features.publico.login.data.datasource.GoogleSignInResult
import com.williamsel.sarc.features.publico.login.data.datasource.api.LoginApi
import com.williamsel.sarc.features.publico.login.data.datasource.api.LoginRequestDto
import com.williamsel.sarc.features.publico.login.data.mapper.toDomain
import com.williamsel.sarc.features.publico.login.data.models.GoogleLoginRequestDto
import com.williamsel.sarc.features.publico.login.domain.entities.Login
import com.williamsel.sarc.features.publico.login.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val googleAuthUiClient: GoogleAuthUiClient
) : LoginRepository {

    override suspend fun login(correo: String, contrasena: String): Login? {
        return try {
            api.login(LoginRequestDto(correo, contrasena)).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun loginConGoogle(idToken: String): Login? {
        return try {
            api.loginConGoogle(GoogleLoginRequestDto(idToken)).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun restaurarSesion(): Login? {
        return try {
            val freshToken = googleAuthUiClient.getFreshIdToken() ?: return null
            api.loginConGoogle(GoogleLoginRequestDto(freshToken)).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override fun isUserSignedIn(): Boolean = googleAuthUiClient.isUserSignedIn()

    override suspend fun signOut() = googleAuthUiClient.signOut()
}
