package com.williamsel.sarc.features.administrador.paneldeadmin.data.datasource.api

import com.williamsel.sarc.features.administrador.paneldeadmin.data.models.PanelReporteDto
import retrofit2.Response
import retrofit2.http.*

interface PanelDeAdminApi {

    /** Obtiene todos los reportes con filtros opcionales */
    @GET("reporte/admin/reportes")
    suspend fun getTodosReportes(
        @Query("incidencia") idIncidencia: Int? = null,
        @Query("estado")     idEstado: Int?     = null
    ): List<PanelReporteDto>

    /** Actualiza el estado de un reporte (PATCH /reporte/{id}/estado) */
    @PATCH("reporte/{id_reporte}/estado")
    suspend fun actualizarEstado(
        @Path("id_reporte") idReporte: Int,
        @Body body: Map<String, Int>
    ): PanelReporteDto

    /** Elimina un reporte */
    @DELETE("reporte/{id_reporte}")
    suspend fun eliminarReporte(
        @Path("id_reporte") idReporte: Int
    ): Response<Unit>
}
