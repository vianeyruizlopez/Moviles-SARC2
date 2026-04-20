package com.williamsel.sarc.features.administrador.mapaadmin.data.datasource.api

import com.williamsel.sarc.features.administrador.mapaadmin.data.models.MapaReporteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapaAdminApi {
    @GET("reporte/admin/reportes")
    suspend fun getReportesParaMapa(
        @Query("incidencia") idIncidencia: Int? = null,
        @Query("estado")     idEstado: Int?     = null
    ): List<MapaReporteDto>
}
