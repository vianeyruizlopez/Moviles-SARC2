package com.williamsel.sarc.features.ciudadano.misreportesciu.data.datasource.api

import com.williamsel.sarc.features.ciudadano.misreportesciu.data.models.ReporteCiudadanoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MisReportesApi {

    @GET("reporte/usuario/{idUsuario}")
    suspend fun getReportes(
        @Path("idUsuario") idUsuario: Int
    ): List<ReporteCiudadanoDto>
}
