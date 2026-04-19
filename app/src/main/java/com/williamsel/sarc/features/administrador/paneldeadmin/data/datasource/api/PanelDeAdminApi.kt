package com.williamsel.sarc.features.administrador.paneldeadmin.data.datasource.api

import com.williamsel.sarc.features.administrador.paneladmin.data.models.ResumenReportesDto
import com.williamsel.sarc.features.administrador.paneldeadmin.data.models.PanelReporteDto
import retrofit2.Response
import retrofit2.http.*

interface PanelDeAdminApi {

    @GET("reporte/estadisticas/global")
    suspend fun getResumenReportes(): ResumenReportesDto

    @GET("reporte/admin/reportes")
    suspend fun getTodosReportes(
        @Query("incidencia") idIncidencia: Int? = null,
        @Query("estado")     idEstado: Int?     = null
    ): List<PanelReporteDto>

    @GET("reporte")
    suspend fun buscarReportes(
        @Query("q") query: String
    ): List<PanelReporteDto>

    @PATCH("reporte/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") idReporte: Int,
        @Body body: Map<String, Int>
    ): Response<Unit>

    @DELETE("reporte/{id}")
    suspend fun eliminarReporte(
        @Path("id") idReporte: Int
    ): Response<Unit>
}
