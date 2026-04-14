package com.williamsel.sarc.features.ciudadano.mapaciu.domain.usecases

import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.repositories.MapaCiudadanoRepository
import javax.inject.Inject

class GetReportesMapaUseCase @Inject constructor(
    private val repository: MapaCiudadanoRepository
) {
    suspend operator fun invoke(): List<ReporteMapa> = repository.getReportes()
}

class GetReportesMapaFiltradosUseCase @Inject constructor(
    private val repository: MapaCiudadanoRepository
) {
    suspend operator fun invoke(
        idIncidencia: Int?,
        idEstado:     Int?
    ): List<ReporteMapa> = repository.getReportesFiltrados(idIncidencia, idEstado)
}
