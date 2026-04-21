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
            remoteDtos.forEach { dao.insert(it.toEntity()) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val cachedEntities = if (estado != null && estado != 0) {
            dao.getListByEstado(estado)
        } else {
            dao.getAllList()
        }

        return cachedEntities.map { it.toDomain() }
    }
}
