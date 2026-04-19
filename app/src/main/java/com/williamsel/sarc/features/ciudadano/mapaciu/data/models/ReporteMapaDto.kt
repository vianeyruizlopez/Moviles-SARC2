package com.williamsel.sarc.features.ciudadano.mapaciu.data.models

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
