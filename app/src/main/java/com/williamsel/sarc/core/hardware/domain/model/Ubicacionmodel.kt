package com.williamsel.sarc.core.hardware.domain.model

data class UbicacionModel(
    val latitud: Double,
    val longitud: Double,
    val direccion: String? = null
)