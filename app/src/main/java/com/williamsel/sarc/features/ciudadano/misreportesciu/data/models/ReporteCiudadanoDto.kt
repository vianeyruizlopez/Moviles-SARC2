package com.williamsel.sarc.features.ciudadano.misreportesciu.data.models

import com.google.gson.annotations.SerializedName

data class ReporteCiudadanoDto(
    @SerializedName("id_reportes")    val idReportes: Int,
    @SerializedName("titulo")         val titulo: String,
    @SerializedName("descripcion")    val descripcion: String,
    @SerializedName("ubicacion")      val ubicacion: String,
    @SerializedName("fecha_reporte")  val fechaReporte: Long,
    @SerializedName("id_incidencias") val idIncidencias: Int,
    @SerializedName("incidencia")     val incidencia: String,
    @SerializedName("id_estado")      val idEstado: Int,
    @SerializedName("estado")         val estado: String
)
