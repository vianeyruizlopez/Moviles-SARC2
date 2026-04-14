package com.williamsel.sarc.features.administrador.mapaadmin.domain.usecases

import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.repositories.MapaAdminRepository
import javax.inject.Inject

class GetReportesMapaUseCase @Inject constructor(
    private val repository: MapaAdminRepository
) {
    suspend operator fun invoke(
        idIncidencia: Int? = null,
        idEstado: Int?     = null
    ): Result<List<MapaReporte>> =
        repository.getReportesParaMapa(idIncidencia, idEstado)
}
