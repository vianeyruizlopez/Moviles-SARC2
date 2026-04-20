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


import android.util.Log

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val usuarioDao: UsuarioDao,
    private val sessionManager: SessionManager
) : LoginRepository {

    override suspend fun login(correo: String, contrasena: String): Login? {
        return try {
            Log.d("SARC_DEBUG", "Iniciando login para: $correo")

            val response = api.login(LoginRequestDto(email = correo, contrasena = contrasena))
            Log.d("SARC_DEBUG", "Respuesta recibida: ${response.token.take(10)}...")

            val domain = response.toDomain().copy(correo = correo)
            
            try {
                usuarioDao.insert(domain.toEntity(correo))
                Log.d("SARC_DEBUG", "Usuario guardado en Room exitosamente")
            } catch (dbError: Exception) {
                Log.e("SARC_DEBUG", "Error al guardar en Room (No bloqueante): ${dbError.message}")
            }
            
            domain
        } catch (e: Exception) {
            Log.e("SARC_DEBUG", "ERROR EN LOGIN: ${e.message}")
            null
        }
    }

    override suspend fun restaurarSesion(): Login? {
        if (sessionManager.isLoggedIn()) {
            val id = sessionManager.getUserId()
            val token = sessionManager.getToken() ?: ""
            val rol = sessionManager.getRol() ?: ""

            val localUser = usuarioDao.getById(id)
            
            return Login(
                id = id,
                token = token,
                rol = rol,
                correo = localUser?.email ?: "",
                nombre = localUser?.nombre ?: "Usuario"
            )
        }
        return null
    }

    override fun isUserSignedIn(): Boolean = sessionManager.isLoggedIn()

    override suspend fun signOut() {
        sessionManager.clearSession()
    }

    override suspend fun loginConGoogle(idToken: String): Login? = null
}
