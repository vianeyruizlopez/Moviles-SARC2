package com.williamsel.sarc.features.ciudadano.misreportesciu.domain.usecases

import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.repositories.MisReportesRepository
import javax.inject.Inject

class GetMisReportesUseCase @Inject constructor(
    private val repository: MisReportesRepository
) {
    // La lógica de negocio vive aquí: decidir cómo filtrar los reportes
    suspend operator fun invoke(
        idUsuario: Int, 
        idEstado: Int? = null, 
        query: String? = null
    ): Result<List<ReporteCiudadano>> {
        return try {
            // El repositorio ahora se encarga de la comunicación filtrada
            val reportes = repository.getReportesByUsuarioYEstado(idUsuario, idEstado ?: 0)
            
            // Si hay una búsqueda, aplicamos el filtro de texto aquí (Lógica de negocio)
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