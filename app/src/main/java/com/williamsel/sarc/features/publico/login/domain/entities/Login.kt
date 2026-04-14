package com.williamsel.sarc.features.publico.login.domain.entities

data class Login(
    val id: Int,
    val correo: String,
    val token: String,
    val rol: String,
    val nombre: String? = null,
    val googleId: String? = null
)
