package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities

data class Reporte(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val estado: EstadoReporte,
    val fechaCreacion: String
)

enum class EstadoReporte {
    PENDIENTE,
    EN_PROCESO,
    RESUELTO
}

data class ResumenReportes(
    val total: Int,
    val pendientes: Int,
    val enProceso: Int,
    val resueltos: Int
)
