package com.williamsel.sarc.features.administrador.mapaadmin.data.repositories

import com.williamsel.sarc.features.administrador.mapaadmin.data.datasource.api.MapaAdminApi
import com.williamsel.sarc.features.administrador.mapaadmin.data.mapper.toDomain
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.repositories.MapaAdminRepository
import javax.inject.Inject

class MapaAdminImpl @Inject constructor(
    private val api: MapaAdminApi
) : MapaAdminRepository {

    override suspend fun getReportesParaMapa(
        idIncidencia: Int?,
        idEstado: Int?
    ): Result<List<MapaReporte>> =
        runCatching {
            api.getReportesParaMapa(idIncidencia, idEstado)
                .filter { it.latitud != 0.0 && it.longitud != 0.0 } // descartar sin coords
                .map { it.toDomain() }
        }
}
