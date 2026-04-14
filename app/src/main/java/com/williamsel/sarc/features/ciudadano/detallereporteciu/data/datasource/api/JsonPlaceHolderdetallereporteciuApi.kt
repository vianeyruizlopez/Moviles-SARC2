package com.williamsel.sarc.features.ciudadano.detallereporteciu.data.datasource.api

import com.williamsel.sarc.features.ciudadano.detallereporteciu.data.models.DetallereporteciuDto
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceHolderdetallereporteciuApi {
    @GET("reporte/{id}")
    suspend fun getDetalleReporte(@Path("id") id: String): DetallereporteciuDto
}
