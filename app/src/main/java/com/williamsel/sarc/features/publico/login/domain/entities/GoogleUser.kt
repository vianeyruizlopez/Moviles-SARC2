package com.williamsel.sarc.features.publico.login.domain.entities

data class GoogleUser(
    val uid: String,
    val nombre: String,
    val email: String,
    val fotoUrl: String?,
    val idToken: String,
    val rol: String
)
