package com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities

import com.google.android.gms.maps.model.BitmapDescriptorFactory

data class ReporteMapaEntity(
    val idReporte:    Int,
    val nombre:       String,
    val descripcion:  String,
    val latitud:      Double,
    val longitud:     Double,
    val idIncidencia: Int?,
    val idEstado:     Int?,
    val fechaReporte: Long?
) {
    /**
     * Color del marcador en el mapa según el estado del reporte:
     * - Pendiente  → rojo
     * - En proceso → amarillo
     * - Resuelto   → verde
     * - Rechazado  → gris
     */
    val markerColor: Float get() = when (idEstado) {
        1    -> BitmapDescriptorFactory.HUE_RED
        2    -> BitmapDescriptorFactory.HUE_YELLOW
        3    -> BitmapDescriptorFactory.HUE_GREEN
        4    -> BitmapDescriptorFactory.HUE_AZURE
        else -> BitmapDescriptorFactory.HUE_RED
    }

    val categoriaLabel: String get() = when (idIncidencia) {
        1    -> "Bache"
        2    -> "Basura"
        3    -> "Alumbrado"
        4    -> "Otro"
        else -> "Incidencia"
    }

    val estadoLabel: String get() = when (idEstado) {
        1    -> "Pendiente"
        2    -> "En proceso"
        3    -> "Resuelto"
        4    -> "Rechazado"
        else -> "Desconocido"
    }
}
