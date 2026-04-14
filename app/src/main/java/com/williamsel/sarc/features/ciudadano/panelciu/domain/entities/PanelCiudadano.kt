package com.williamsel.sarc.features.ciudadano.panelciu.domain.entities

data class PanelCiudadano(
    val idUsuario: Int,
    val nombreCompleto: String,
    val totalReportes: Int,
    val pendientes: Int,
    val enProceso: Int,
    val resueltos: Int
)
