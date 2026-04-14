package com.williamsel.sarc.features.superadmin.usuariosuperadmin.presentacion.viewmodels

import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.UsuarioSistema

data class UsuarioSuperAdminUiState(
    val isLoading: Boolean              = false,
    val totalUsuarios: Int              = 0,
    val totalAdministradores: Int       = 0,
    val totalCiudadanos: Int            = 0,
    val administradores: List<UsuarioSistema> = emptyList(),
    val ciudadanos: List<UsuarioSistema>      = emptyList(),
    val errorMessage: String?           = null
)
