package com.williamsel.sarc.features.superadmin.crearusuario.domain.entities

data class Administrador(
    val idUsuario: Int,
    val nombreCompleto: String,
    val email: String,
    val activo: Boolean
)

data class NuevoAdministrador(
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val email: String,
    val contrasena: String
)
