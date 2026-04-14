package com.williamsel.sarc.features.ciudadano.mapaciu.data.models

/** Reporte tal como lo devuelve el backend para el mapa */
data class ReporteMapaDto(
    val idReporte:    Int,
    val nombre:       String,
    val descripcion:  String,
    val latitud:      Double,
    val longitud:     Double,
    val idIncidencia: Int?,
    val idEstado:     Int?,
    val fechaReporte: Long?
)
