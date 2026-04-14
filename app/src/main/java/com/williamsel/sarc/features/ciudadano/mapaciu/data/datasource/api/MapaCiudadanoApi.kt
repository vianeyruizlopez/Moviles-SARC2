package com.williamsel.sarc.features.ciudadano.mapaciu.data.datasource.api

import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.ReporteMapaDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapaCiudadanoApi {

    /** Todos los reportes con coordenadas para pintar marcadores */
    @GET("reporte/mapa")
    suspend fun getReportes(): List<ReporteMapaDto>

    /** Filtrados por categoría y/o estado */
    @GET("reporte/mapa")
    suspend fun getReportesFiltrados(
        @Query("incidencia") idIncidencia: Int? = null,
        @Query("estado")     idEstado:     Int? = null
    ): List<ReporteMapaDto>
}
