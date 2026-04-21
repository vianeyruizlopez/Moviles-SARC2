package com.williamsel.sarc.features.administrador.reportesadmin.domain.usecases

import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.domain.repositories.ReportesAdminRepository
import javax.inject.Inject

class GetReportesAdminUseCase @Inject constructor(
    private val repository: ReportesAdminRepository
) {
    suspend operator fun invoke(
        idEstado: Int? = null,
        query: String? = null
    ): Result<List<ReporteAdmin>> {
        return try {
            val reportes = repository.getReportes(idEstado, query)

            val filtrados = if (!query.isNullOrBlank()) {
                reportes.filter {
                    it.titulo.contains(query, ignoreCase = true) ||
                            it.descripcion.contains(query, ignoreCase = true) ||
                            it.nombreUsuario.contains(query, ignoreCase = true) ||
                            (it.ubicacion?.contains(query, ignoreCase = true) ?: false)
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