package com.williamsel.sarc.features.administrador.reportesadmin.data.datasource.api

import com.williamsel.sarc.features.administrador.reportesadmin.data.models.ReporteAdminDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportesAdminApi {

    @GET("reporte")
    suspend fun getReportes(
        @Query("estado") estado: Int? = null,
        @Query("q") query: String? = null
    ): List<ReporteAdminDto>
}
