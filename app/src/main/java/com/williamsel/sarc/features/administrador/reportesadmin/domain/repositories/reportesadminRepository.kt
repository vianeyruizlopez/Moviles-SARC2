package com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories

import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin

interface ReportesAdminRepository {
    suspend fun getReportesByUsuario(idUsuario: Int, estado: Int? = null): List<ReporteAdmin>
}
