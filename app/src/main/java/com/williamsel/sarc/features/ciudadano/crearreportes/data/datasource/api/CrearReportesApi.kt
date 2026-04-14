package com.williamsel.sarc.features.ciudadano.crearreportes.data.datasource.api

import com.williamsel.sarc.features.ciudadano.crearreportes.data.models.CategoriaDto
import com.williamsel.sarc.features.ciudadano.crearreportes.data.models.CrearReporteRequest
import com.williamsel.sarc.features.ciudadano.crearreportes.data.models.CrearReporteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CrearReportesApi {

    @Multipart
    @POST("reporte")
    suspend fun crearReporte(
        @Part("titulo")        titulo: RequestBody,
        @Part("descripcion")   descripcion: RequestBody,
        @Part("id_usuario")    idUsuario: RequestBody,
        @Part("id_incidencia") idIncidencia: RequestBody,
        @Part("latitud")       latitud: RequestBody,
        @Part("longitud")      longitud: RequestBody,
        @Part("id_estado")     idEstado: RequestBody,
        @Part("ubicacion")     ubicacion: RequestBody,
        @Part                  imagen: MultipartBody.Part? = null
    ): CrearReporteResponse

    @GET("catalogo/incidencias")
    suspend fun getCategorias(): List<CategoriaDto>
}
