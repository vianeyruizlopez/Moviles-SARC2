package com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.entities

data class PanelSuperAdmin(
    val nombreCompleto: String,
    val totalReportes: Int,
    val pendientes: Int,
    val enProceso: Int,
    val resueltos: Int,
    val totalAdmins: Int,
    val totalCiudadanos: Int
)
