package com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities

data class ReporteCiudadano(
    val idReporte: Int?,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val fechaReporte: Long,
    val incidencia: String,
    val estado: String,
    val idEstado: Int,
    val puedeEditar: Boolean,
    val imagen: String? = null
)