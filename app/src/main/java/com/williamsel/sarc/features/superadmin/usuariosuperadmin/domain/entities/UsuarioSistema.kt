package com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities

data class UsuarioSistema(
    val idUsuario: Int,
    val nombreCompleto: String,
    val email: String,
    val rol: RolUsuario,
    val activo: Boolean
)

enum class RolUsuario(val label: String) {
    SUPER_ADMIN("Super Admin"),
    ADMIN("Admin"),
    CIUDADANO("Ciudadano")
}

data class ResumenUsuarios(
    val totalUsuarios: Int,
    val totalAdministradores: Int,
    val totalCiudadanos: Int,
    val administradores: List<UsuarioSistema>,
    val ciudadanos: List<UsuarioSistema>
)
