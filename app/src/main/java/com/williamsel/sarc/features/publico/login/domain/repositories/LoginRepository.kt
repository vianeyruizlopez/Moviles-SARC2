package com.williamsel.sarc.features.publico.login.domain.repositories

import com.williamsel.sarc.features.publico.login.domain.entities.Login

interface LoginRepository {
    suspend fun login(correo: String, contrasena: String): Login?
    suspend fun loginConGoogle(idToken: String): Login?
    suspend fun restaurarSesion(): Login?
    fun isUserSignedIn(): Boolean
    suspend fun signOut()
}
