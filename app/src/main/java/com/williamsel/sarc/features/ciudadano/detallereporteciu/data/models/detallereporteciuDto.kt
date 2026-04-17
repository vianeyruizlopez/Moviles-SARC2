package com.williamsel.sarc.features.ciudadano.detallereporteciu.data.models

import com.google.gson.annotations.SerializedName

data class DetallereporteciuDto(
    @SerializedName("id_reporte")      val id: Int,
    @SerializedName("titulo")          val titulo: String,
    @SerializedName("descripcion")     val descripcion: String,
    @SerializedName("nombreIncidencia") val categoria: String,
    @SerializedName("id_incidencia")   val idIncidencia: Int, // Agregado para integridad
    @SerializedName("imagen")          val imagenUrl: String? = null,
    @SerializedName("nombreEstado")    val estado: String,
    @SerializedName("ubicacion")       val direccion: String,
    @SerializedName("latitud")         val latitud: Double,
    @SerializedName("longitud")        val longitud: Double,
    @SerializedName("fecha")           val fecha: String
)
