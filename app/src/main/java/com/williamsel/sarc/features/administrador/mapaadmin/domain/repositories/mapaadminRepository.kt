package com.williamsel.sarc.features.administrador.mapaadmin.domain.repositories

import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte

interface MapaAdminRepository {
    suspend fun getReportesParaMapa(
        idIncidencia: Int? = null,
        idEstado: Int?     = null
    ): Result<List<MapaReporte>>
}
