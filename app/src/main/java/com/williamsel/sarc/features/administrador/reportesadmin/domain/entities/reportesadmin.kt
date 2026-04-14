package com.williamsel.sarc.features.administrador.reportesadmin.domain.entities

data class ReporteAdmin(
    val idReporte: Int,
    val idUsuario: Int,
    val nombreUsuario: String,
    val nombreIncidencia: String,
    val nombreEstado: String,
    val idIncidencia: Int,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String?,
    val latitud: Double?,
    val longitud: Double?,
    val idEstado: Int,
    val imagen: String?,
    val fecha: String
)
