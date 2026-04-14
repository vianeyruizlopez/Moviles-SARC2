package com.williamsel.sarc.features.ciudadano.misreportesciu.domain.repositories

import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
import kotlinx.coroutines.flow.Flow

interface MisReportesRepository {
    fun getReportesByUsuario(idUsuario: Int): Flow<List<ReporteCiudadano>>
    suspend fun getReportesByUsuarioYEstado(idUsuario: Int, idEstado: Int): List<ReporteCiudadano>
}
