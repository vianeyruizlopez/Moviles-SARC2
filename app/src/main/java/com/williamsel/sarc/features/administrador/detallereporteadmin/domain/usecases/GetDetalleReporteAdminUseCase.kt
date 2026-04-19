package com.williamsel.sarc.features.administrador.detallereporteadmin.domain.usecases

import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.repositories.DetalleReporteAdminRepository
import javax.inject.Inject

class GetDetalleReporteAdminUseCase @Inject constructor(
    private val repository: DetalleReporteAdminRepository
) {
    suspend operator fun invoke(idReporte: Int): DetalleReporteAdmin {
        return repository.getReporteById(idReporte)
    }
}
