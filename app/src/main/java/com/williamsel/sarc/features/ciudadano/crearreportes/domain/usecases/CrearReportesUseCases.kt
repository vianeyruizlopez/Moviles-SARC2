package com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases

import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.NuevoReporte
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.repositories.CrearReportesRepository
import javax.inject.Inject

class EnviarReporteUseCase @Inject constructor(
    private val repository: CrearReportesRepository
) {
    suspend operator fun invoke(idUsuario: Int, reporte: NuevoReporte): Result<Int> =
        repository.enviarReporte(idUsuario, reporte)
}

class GetCategoriasUseCase @Inject constructor(
    private val repository: CrearReportesRepository
) {
    suspend operator fun invoke(): List<CategoriaIncidencia> =
        repository.getCategorias()
}
