package com.williamsel.sarc.features.ciudadano.misreportesciu.data.models

import com.google.gson.annotations.SerializedName

data class ReporteCiudadanoDto(
    @SerializedName("id_reporte")      val idReporte: Int,
    @SerializedName("titulo")          val titulo: String,
    @SerializedName("descripcion")     val descripcion: String,
    @SerializedName("ubicacion")       val ubicacion: String,
    @SerializedName("nombreIncidencia") val nombreIncidencia: String,
    @SerializedName("nombreEstado")     val nombreEstado: String,
    @SerializedName("id_incidencia")   val idIncidencia: Int,
    @SerializedName("id_estado")       val idEstado: Int,
    @SerializedName("imagen")          val imagen: String?,
    @SerializedName("fecha")           val fecha: String
)