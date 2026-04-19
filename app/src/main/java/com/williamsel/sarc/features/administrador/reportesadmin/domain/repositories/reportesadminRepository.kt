package com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories

import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin

interface ReportesAdminRepository {
    suspend fun getReportes(estado: Int? = null, query: String? = null): List<ReporteAdmin>
}
