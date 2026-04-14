package com.williamsel.sarc.features.publico.login.data.datasource.api

import com.williamsel.sarc.features.publico.login.data.models.GoogleLoginRequestDto
import com.williamsel.sarc.features.publico.login.data.models.LoginDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    /** Login tradicional con correo y contraseña */
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): LoginDto

    /**
     * Envía el idToken de Firebase al backend Ktor.
     * Se usa tanto para login con Google como para restaurar sesión activa.
     */
    @POST("auth/google")
    suspend fun loginConGoogle(
        @Body body: GoogleLoginRequestDto
    ): LoginDto
}

data class LoginRequestDto(
    val email: String,
    val contrasena: String
)
