package com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases

import com.williamsel.sarc.core.hardware.domain.LocationManager
import com.williamsel.sarc.core.hardware.domain.model.UbicacionModel
import javax.inject.Inject

class ObtenerUbicacionUseCase @Inject constructor(
    private val locationManager: LocationManager
) {
    suspend operator fun invoke(): UbicacionModel? {
        return if (locationManager.tienePermisoUbicacion()) {
            locationManager.obtenerUbicacion()
        } else {
            null
        }
    }
}