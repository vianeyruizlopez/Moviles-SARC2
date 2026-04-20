package com.williamsel.sarc.features.perfil.domain.entities

data class Usuario(
    val id: Int,
    val nombreCompleto: String,
    val email: String,
    val rol: Int
)
