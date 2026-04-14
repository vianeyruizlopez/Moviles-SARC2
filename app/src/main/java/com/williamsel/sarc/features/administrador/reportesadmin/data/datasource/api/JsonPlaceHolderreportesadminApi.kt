package com.williamsel.sarc.features.administrador.reportesadmin.data.datasource.api

import com.williamsel.sarc.features.administrador.reportesadmin.data.models.ReportesAdminDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportesAdminApi {

    @GET("reporte/usuario/{id_usuario}")
    suspend fun getReportesByUsuario(
        @Path("id_usuario") idUsuario: Int,
        @Query("estado") estado: Int? = null
    ): List<ReportesAdminDto>
}
