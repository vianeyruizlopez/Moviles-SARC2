package com.williamsel.sarc.features.publico.registro.domain.entities

data class UsuarioRegistrado(
    val id: Int,
    val nombre: String,
    val correo: String,
    val token: String,
    val rol: String,
    val googleId: String? = null
)
