package com.williamsel.sarc.features.administrador.paneladmin.domain.usecases

import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneladmin.domain.repositories.PanelPrincipalAdminRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener las estadísticas globales del panel de administración.
 */
class GetResumenReportesUseCase @Inject constructor(
    private val repository: PanelPrincipalAdminRepository
) {
    suspend operator fun invoke(): Result<ResumenReportes> =
        repository.getResumenReportes()
}
