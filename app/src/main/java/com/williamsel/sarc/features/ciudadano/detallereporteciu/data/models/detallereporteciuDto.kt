package com.williamsel.sarc.features.ciudadano.detallereporteciu.data.models

import com.google.gson.annotations.SerializedName

data class DetallereporteciuDto(
    @SerializedName("id")           val id: String,
    @SerializedName("titulo")       val titulo: String,
    @SerializedName("descripcion")  val descripcion: String,
    @SerializedName("categoria")    val categoria: String,
    @SerializedName("icono_url")    val iconoUrl: String?   = null,
    @SerializedName("imagen_url")   val imagenUrl: String?  = null,
    @SerializedName("estado")       val estado: String,
    @SerializedName("direccion")    val direccion: String,
    @SerializedName("latitud")      val latitud: Double,
    @SerializedName("longitud")     val longitud: Double,
    @SerializedName("fecha")        val fecha: String
)
