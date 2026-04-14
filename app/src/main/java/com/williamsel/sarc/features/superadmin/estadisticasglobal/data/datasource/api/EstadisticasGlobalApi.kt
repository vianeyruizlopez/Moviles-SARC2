package com.williamsel.sarc.features.superadmin.estadisticasglobal.data.datasource.api

import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.models.EstadisticasGlobalDto
import retrofit2.http.GET

interface EstadisticasGlobalApi {

    @GET("reporte/estadisticas/global")
    suspend fun getEstadisticas(): EstadisticasGlobalDto
}
