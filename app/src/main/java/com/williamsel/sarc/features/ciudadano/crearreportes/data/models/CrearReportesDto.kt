package com.williamsel.sarc.features.ciudadano.crearreportes.data.models

import com.google.gson.annotations.SerializedName

data class CrearReporteRequest(
    @SerializedName("titulo")         val titulo: String,
    @SerializedName("descripcion")    val descripcion: String,
    @SerializedName("ubicacion")      val ubicacion: String,
    @SerializedName("latitud")        val latitud: Double?,
    @SerializedName("longitud")       val longitud: Double?,
    @SerializedName("id_usuario")     val idUsuario: Int,
    @SerializedName("id_incidencias") val idIncidencias: Int,
    @SerializedName("id_estado")      val idEstado: Int = 1
)

data class CrearReporteResponse(
    @SerializedName("id_reportes") val idReportes: Int,
    @SerializedName("mensaje")     val mensaje: String
)

data class CategoriaDto(
    @SerializedName("id_incidencias") val id: Int,
    @SerializedName("nombre")         val nombre: String
)
