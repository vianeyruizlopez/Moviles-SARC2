package com.williamsel.sarc.features.publico.registro.data.models

import com.google.gson.annotations.SerializedName

data class RegistroRequestDto(
    val nombre: String,
    @SerializedName("email") val correo: String,
    val contrasena: String,
    @SerializedName("primerApellido") val primerApellido: String = "",
    @SerializedName("segundoApellido") val segundoApellido: String = "",
    val edad: Int = 18,
    val idRol: Int = 2
)

data class RegistroGoogleRequestDto(
    val idToken: String
)

data class RegistroDto(
    val id: Int,
    val nombre: String,
    @SerializedName("email") val correo: String,
    val token: String = "",
    @SerializedName("nombreRol") val rol: String = "Ciudadano",
    val googleId: String? = null
)