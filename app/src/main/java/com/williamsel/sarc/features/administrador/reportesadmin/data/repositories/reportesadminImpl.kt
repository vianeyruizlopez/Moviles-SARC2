package com.williamsel.sarc.features.administrador.reportesadmin.data.repositories

import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.features.administrador.reportesadmin.data.datasource.api.ReportesAdminApi
import com.williamsel.sarc.features.administrador.reportesadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.reportesadmin.data.mapper.toEntity
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories.ReportesAdminRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ReportesAdminRepositoryImpl @Inject constructor(
    private val api: ReportesAdminApi,
    private val dao: ReporteDao
) : ReportesAdminRepository {

    override suspend fun getReportes(estado: Int?, query: String?): List<ReporteAdmin> {
        return try {
            val remoteDtos = api.getReportes(estado, query)

            //rooms
            remoteDtos.forEach { dto ->
                dao.insert(dto.toEntity())
            }
            remoteDtos.map { it.toDomain() }
            
        } catch (e: Exception) {
            // si no hay red esta en el cacche por medio de rooms
            val cachedEntities = dao.getAll().first()

            if (cachedEntities.isNotEmpty()) {
                cachedEntities.map { it.toDomain() }
            } else {
                throw e
            }
        }
    }
}
