package com.williamsel.sarc.features.administrador.mapaadmin.data.datasource.api

import com.williamsel.sarc.features.administrador.mapaadmin.data.models.MapaReporteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapaAdminApi {

    /**
     * Obtiene todos los reportes para mostrar en el mapa del admin.
     * GET /reporte/mapa?incidencia={id}&estado={id}
     */
    @GET("reporte/mapa")
    suspend fun getReportesParaMapa(
        @Query("incidencia") idIncidencia: Int? = null,
        @Query("estado")     idEstado: Int?     = null
    ): List<MapaReporteDto>
}
