package com.williamsel.sarc.features.publico.login.data.repositories

import com.williamsel.sarc.core.database.dao.UsuarioDao
import com.williamsel.sarc.core.session.SessionManager
import com.williamsel.sarc.features.publico.login.data.datasource.api.LoginApi
import com.williamsel.sarc.features.publico.login.data.datasource.api.LoginRequestDto
import com.williamsel.sarc.features.publico.login.data.mapper.toDomain
import com.williamsel.sarc.features.publico.login.data.mapper.toEntity
import com.williamsel.sarc.features.publico.login.domain.entities.Login
import com.williamsel.sarc.features.publico.login.domain.repositories.LoginRepository
import javax.inject.Inject


class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val usuarioDao: UsuarioDao,
    private val sessionManager: SessionManager
) : LoginRepository {

    override suspend fun login(correo: String, contrasena: String): Login? {
        return try {
            // api
            val response = api.login(LoginRequestDto(correo, contrasena))
            val domain = response.toDomain().copy(correo = correo)
            //local rooms
            usuarioDao.insert(domain.toEntity(correo))
            domain
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun restaurarSesion(): Login? {
        if (sessionManager.isLoggedIn()) {
            val id = sessionManager.getUserId()
            val token = sessionManager.getToken() ?: ""
            val rol = sessionManager.getRol() ?: ""

            return usuarioDao.getById(id)?.let { localUser ->
                Login(
                    id = id,
                    token = token,
                    rol = rol,
                    correo = localUser.email,
                    nombre = localUser.nombre
                )
            }
        }
        return null
    }

    override fun isUserSignedIn(): Boolean = sessionManager.isLoggedIn()

    override suspend fun signOut() {
        sessionManager.clearSession()
    }

    override suspend fun loginConGoogle(idToken: String): Login? = null
}
