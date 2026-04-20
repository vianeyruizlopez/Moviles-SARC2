package com.williamsel.sarc.features.perfil.data.models

data class PerfilDto(
    val id: Int,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String?,
    val email: String,
    val edad: Int?,
    val idRol: Int?
)