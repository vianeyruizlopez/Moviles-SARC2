package com.williamsel.sarc.features.publico.login.data.models

data class GoogleLoginRequestDto(
    val idToken: String
)

/** Respuesta real del backend: solo contiene el token JWT */
data class LoginDto(
    val token: String
)
