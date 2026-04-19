package com.williamsel.sarc.features.ciudadano.mapaciu.data.datasource.api

import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.ReporteMapaDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapaCiudadanoApi {

    @GET("reporte/mapa")
    suspend fun getReportes(
        @Query("incidencia") idIncidencia: Int? = null,
        @Query("estado")     idEstado:     Int? = null
    ): List<ReporteMapaDto>
}
