package com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities

data class ReporteMapa(
    val idReporte:    Int,
    val nombre:       String,
    val descripcion:  String,
    val latitud:      Double,
    val longitud:     Double,
    val idIncidencia: Int?,
    val idEstado:     Int?,
    val fechaReporte: Long?
) {
    val categoriaLabel: String get() = when (idIncidencia) {
        1    -> "Bache"
        2    -> "Basura"
        3    -> "Luminaria"
        else -> "Incidencia"
    }

    val estadoLabel: String get() = when (idEstado) {
        1    -> "Pendiente"
        2    -> "En Proceso"
        3    -> "Resuelto"
        else -> "Desconocido"
    }
}