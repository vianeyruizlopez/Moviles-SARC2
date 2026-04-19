package com.williamsel.sarc.features.administrador.paneladmin.data.datasource.api

import com.williamsel.sarc.features.administrador.paneladmin.data.models.ResumenReportesDto
import retrofit2.http.GET

interface PanelPrincipalAdminApi {
    @GET("reporte/estadisticas/global")
    suspend fun getResumenReportes(): ResumenReportesDto
}
