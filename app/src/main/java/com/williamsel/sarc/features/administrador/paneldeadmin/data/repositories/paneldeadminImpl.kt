package com.williamsel.sarc.features.administrador.paneldeadmin.data.repositories

import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.mapper.toDomain as toResumenDomain
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneldeadmin.data.datasource.api.PanelDeAdminApi
import com.williamsel.sarc.features.administrador.paneldeadmin.data.mapper.toDomain as toPanelReporteDomain
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.repositories.PanelDeAdminRepository
import javax.inject.Inject

class PanelDeAdminImpl @Inject constructor(
    private val api: PanelDeAdminApi
) : PanelDeAdminRepository {

    override suspend fun getResumenReportes(): Result<ResumenReportes> =
        runCatching { api.getResumenReportes().toResumenDomain() }

    override suspend fun getTodosReportes(idIncidencia: Int?, idEstado: Int?): Result<List<PanelReporte>> =
        runCatching { api.getTodosReportes(idIncidencia, idEstado).map { it.toPanelReporteDomain() } }

    override suspend fun buscarReportes(query: String): Result<List<PanelReporte>> =
        runCatching { api.buscarReportes(query).map { it.toPanelReporteDomain() } }

    override suspend fun actualizarEstado(idReporte: Int, idEstado: Int): Result<Unit> =
        runCatching {
            val response = api.actualizarEstado(idReporte, mapOf("id_estado" to idEstado))
            if (!response.isSuccessful) throw Exception("Error al actualizar estado")
        }

    override suspend fun eliminarReporte(idReporte: Int): Result<Unit> =
        runCatching {
            val response = api.eliminarReporte(idReporte)
            if (!response.isSuccessful) throw Exception("Error al eliminar reporte")
        }
}
