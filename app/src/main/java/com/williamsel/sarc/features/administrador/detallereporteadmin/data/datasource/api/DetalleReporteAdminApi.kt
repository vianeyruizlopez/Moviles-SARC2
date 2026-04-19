package com.williamsel.sarc.features.administrador.detallereporteadmin.data.datasource.api

import com.williamsel.sarc.features.administrador.detallereporteadmin.data.models.DetalleReporteAdminDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface DetalleReporteAdminApi {

    @GET("reporte/{id_reporte}")
    suspend fun getReporteById(
        @Path("id_reporte") idReporte: Int
    ): DetalleReporteAdminDto

    @PATCH("reporte/{id_reporte}/estado")
    suspend fun actualizarEstado(
        @Path("id_reporte") idReporte: Int,
        @Body body: Map<String, Int>
    ): Map<String, String>
}
