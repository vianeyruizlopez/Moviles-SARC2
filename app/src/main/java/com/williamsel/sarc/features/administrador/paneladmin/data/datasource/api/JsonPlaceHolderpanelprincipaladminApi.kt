package com.williamsel.sarc.features.administrador.paneladmin.data.datasource.api

import com.williamsel.sarc.features.administrador.paneladmin.data.models.ReporteDto
import com.williamsel.sarc.features.administrador.paneladmin.data.models.ResumenReportesDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PanelPrincipalAdminApi {
    @GET("reporte/estadisticas/globalAdmi")
    suspend fun getResumenReportes(): ResumenReportesDto

    @GET("reporte")
    suspend fun getReportes(): List<ReporteDto>

    @GET("reporte/{id}")
    suspend fun getReporteById(@Path("id") id: Int): ReporteDto
}
