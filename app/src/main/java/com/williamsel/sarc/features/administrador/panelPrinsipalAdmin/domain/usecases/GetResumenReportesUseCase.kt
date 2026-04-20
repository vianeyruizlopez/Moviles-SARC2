package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.usecases

import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.repositories.PanelPrincipalAdminRepository
import javax.inject.Inject


class GetResumenReportesUseCase @Inject constructor(
    private val repository: PanelPrincipalAdminRepository
) {
    suspend operator fun invoke(): Result<ResumenReportes> =
        repository.getResumenReportes()
}
