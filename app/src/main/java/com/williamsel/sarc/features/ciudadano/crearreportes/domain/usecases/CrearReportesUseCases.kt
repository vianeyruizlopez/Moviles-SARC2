package com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases

import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.NuevoReporte
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.repositories.CrearReportesRepository
import javax.inject.Inject

class EnviarReporteUseCase @Inject constructor(
    private val repository: CrearReportesRepository
) {
    suspend operator fun invoke(idUsuario: Int, reporte: NuevoReporte): Result<Int> {
        if (reporte.titulo.isBlank()) {
            return Result.failure(Exception("El título no puede estar vacío"))
        }
        if (reporte.descripcion.length < 10) {
            return Result.failure(Exception("La descripción debe tener al menos 10 caracteres"))
        }
        if (reporte.idIncidencia <= 0) {
            return Result.failure(Exception("Debes seleccionar una categoría válida"))
        }
        return repository.enviarReporte(idUsuario, reporte)
    }
}

class GetCategoriasUseCase @Inject constructor(
    private val repository: CrearReportesRepository
) {
    suspend operator fun invoke(): List<CategoriaIncidencia> {
        return repository.getCategorias()
    }
}