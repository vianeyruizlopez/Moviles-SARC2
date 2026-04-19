package com.williamsel.sarc.features.administrador.detallereporteadmin.domain.usecases

import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.repositories.DetalleReporteAdminRepository
import javax.inject.Inject

class ActualizarEstadoReporteUseCase @Inject constructor(
    private val repository: DetalleReporteAdminRepository
) {
    suspend operator fun invoke(idReporte: Int, idEstado: Int) {
        repository.actualizarEstado(idReporte, idEstado)
    }
}
