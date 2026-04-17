package com.williamsel.sarc.features.ciudadano.registro.data.models

data class RegistroRequestDto(
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val email: String,
    val contrasena: String,
    val edad: Int,
    val idRol: Int = 2
)

data class RegistroDto(
    val id: Int,
    val nombre: String,
    val email: String,
    val primerApellido: String,
    val segundoApellido: String,
    val edad: Int,
    val idRol: Int,
    val nombreRol: String,
    val contrasena: String? = null,
    val googleId: String? = null
)

data class RegistroGoogleRequestDto(
    val idToken: String
)
