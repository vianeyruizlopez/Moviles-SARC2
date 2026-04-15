package com.williamsel.sarc.features.ciudadano.misreportesciu.data.repositories

import com.williamsel.sarc.core.database.dao.CatEstadoReporteDao
import com.williamsel.sarc.core.database.dao.CatIncidenciasDao
import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.features.ciudadano.misreportesciu.data.datasource.api.MisReportesApi
import com.williamsel.sarc.features.ciudadano.misreportesciu.data.mapper.toDomain
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.repositories.MisReportesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MisReportesRepositoryImpl @Inject constructor(
    private val api: MisReportesApi,
    private val reporteDao: ReporteDao,
    private val catIncidenciasDao: CatIncidenciasDao,
    private val catEstadoReporteDao: CatEstadoReporteDao
) : MisReportesRepository {

    override fun getReportesByUsuario(idUsuario: Int): Flow<List<ReporteCiudadano>> =
        reporteDao.getByUsuario(idUsuario).map { entities ->
            entities.map { entity ->
                val incidencia = entity.idIncidencia?.let {
                    catIncidenciasDao.getById(it)?.nombre
                } ?: "Otro"
                val estado = entity.idEstado?.let {
                    catEstadoReporteDao.getById(it)?.nombre
                } ?: "Pendiente"
                entity.toDomain(incidencia, estado)
            }
        }

    override suspend fun getReportesByUsuarioYEstado(
        idUsuario: Int,
        idEstado: Int
    ): List<ReporteCiudadano> {
        return try {
            val response = api.getReportes(
                idUsuario = idUsuario,
                estado    = if (idEstado == 0) null else idEstado
            )
            response.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}