package com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.entities

data class Detallereporteciu(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val iconoUrl: String?,
    val imagenUrl: String?,
    val estado: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val fecha: String
)
