package com.williamsel.sarc.features.publico.login.data.datasource.api

import com.williamsel.sarc.features.publico.login.data.models.GoogleLoginRequestDto
import com.williamsel.sarc.features.publico.login.data.models.LoginDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): LoginDto

    @POST("auth/google")
    suspend fun loginConGoogle(
        @Body body: GoogleLoginRequestDto
    ): LoginDto
}

data class LoginRequestDto(
    val email: String,
    val contrasena: String
)
