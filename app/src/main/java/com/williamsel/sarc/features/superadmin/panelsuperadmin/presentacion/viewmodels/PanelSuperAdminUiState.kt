package com.williamsel.sarc.features.superadmin.panelsuperadmin.presentacion.viewmodels

data class PanelSuperAdminUiState(
    val isLoading: Boolean      = false,
    val nombreCompleto: String  = "",
    val totalReportes: Int      = 0,
    val pendientes: Int         = 0,
    val enProceso: Int          = 0,
    val resueltos: Int          = 0,
    val totalAdmins: Int        = 0,
    val totalCiudadanos: Int    = 0,
    val errorMessage: String?   = null
)
