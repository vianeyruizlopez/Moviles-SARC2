package com.williamsel.sarc.core.hardware.domain

import com.williamsel.sarc.core.hardware.domain.model.UbicacionModel

interface LocationManager {
    suspend fun obtenerUbicacion(): UbicacionModel?
    fun tienePermisoUbicacion(): Boolean
}