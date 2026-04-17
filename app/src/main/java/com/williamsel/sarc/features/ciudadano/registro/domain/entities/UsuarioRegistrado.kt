package com.williamsel.sarc.features.ciudadano.registro.domain.entities

data class UsuarioRegistrado(
    val id: Int,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val correo: String,
    val edad: Int,
    val rol: String,
    val googleId: String? = null
)
