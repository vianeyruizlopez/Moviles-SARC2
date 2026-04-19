package com.williamsel.sarc.features.administrador.reportesadmin.domain.usecases

import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories.ReportesAdminRepository
import javax.inject.Inject

class GetReportesAdminUseCase @Inject constructor(
    private val repository: ReportesAdminRepository
) {
    suspend operator fun invoke(estado: Int? = null, query: String? = null): List<ReporteAdmin> {
        return repository.getReportes(estado, query)
    }
}
