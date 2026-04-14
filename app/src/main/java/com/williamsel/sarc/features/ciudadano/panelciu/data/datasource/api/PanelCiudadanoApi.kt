package com.williamsel.sarc.features.ciudadano.panelciu.data.datasource.api

import com.williamsel.sarc.features.ciudadano.panelciu.data.models.PanelCiudadanoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PanelCiudadanoApi {

    @GET("reporte/usuario/{idUsuario}/estadisticas")
    suspend fun getPanelData(
        @Path("idUsuario") idUsuario: Int
    ): PanelCiudadanoDto
}
