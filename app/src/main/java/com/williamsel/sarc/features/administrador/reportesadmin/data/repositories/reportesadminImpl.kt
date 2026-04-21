package com.williamsel.sarc.features.administrador.reportesadmin.data.repositories

import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.features.administrador.reportesadmin.data.datasource.api.ReportesAdminApi
import com.williamsel.sarc.features.administrador.reportesadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.reportesadmin.data.mapper.toEntity
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories.ReportesAdminRepository
import javax.inject.Inject

class ReportesAdminRepositoryImpl @Inject constructor(
    private val api: ReportesAdminApi,
    private val dao: ReporteDao
) : ReportesAdminRepository {

    override suspend fun getReportes(estado: Int?, query: String?): List<ReporteAdmin> {
        try {
            val remoteDtos = api.getReportes(estado, query)
            
            remoteDtos.forEach { dto ->
                dao.insert(dto.toEntity())
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }

        val cachedEntities = dao.getAllList()
        
        val reportesDomain = cachedEntities.map { it.toDomain() }

        return if (estado != null && estado != 0) {
            reportesDomain.filter { it.idEstado == estado }
        } else {
            reportesDomain
        }
    }
}
