package com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.entities

data class Detallereporteciu(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val iconoUrl: String? = null,
    val imagenUrl: String? = null,
    val estado: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val fecha: String
)
