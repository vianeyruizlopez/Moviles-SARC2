package com.williamsel.sarc.features.ciudadano.misreportesciu.domain.usecases

import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.repositories.MisReportesRepository
import javax.inject.Inject

class GetMisReportesUseCase @Inject constructor(
    private val repository: MisReportesRepository
) {
    suspend operator fun invoke(
        idUsuario: Int, 
        idEstado: Int? = null, 
        query: String? = null
    ): Result<List<ReporteCiudadano>> {
        return try {
            val reportes = repository.getReportesByUsuarioYEstado(idUsuario, idEstado ?: 0)
            
            val filtrados = if (!query.isNullOrBlank()) {
                reportes.filter { 
                    it.titulo.contains(query, ignoreCase = true) || 
                    it.descripcion.contains(query, ignoreCase = true) ||
                    it.ubicacion.contains(query, ignoreCase = true)
                }
            } else {
                reportes
            }
            
            Result.success(filtrados)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}