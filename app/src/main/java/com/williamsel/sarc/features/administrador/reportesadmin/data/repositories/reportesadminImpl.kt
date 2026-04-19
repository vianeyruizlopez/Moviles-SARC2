package com.williamsel.sarc.features.administrador.reportesadmin.data.repositories

import com.williamsel.sarc.features.administrador.reportesadmin.data.datasource.api.ReportesAdminApi
import com.williamsel.sarc.features.administrador.reportesadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories.ReportesAdminRepository
import javax.inject.Inject

class ReportesAdminRepositoryImpl @Inject constructor(
    private val api: ReportesAdminApi
) : ReportesAdminRepository {

    override suspend fun getReportes(estado: Int?, query: String?): List<ReporteAdmin> {
        return try {
            api.getReportes(estado, query).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
