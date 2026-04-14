package com.williamsel.sarc.features.administrador.paneladmin.data.models

import com.google.gson.annotations.SerializedName

data class ReporteDto(
    @SerializedName("id")           val id: Int,
    @SerializedName("titulo")       val titulo: String,
    @SerializedName("descripcion")  val descripcion: String,
    @SerializedName("estado")       val estado: String,
    @SerializedName("fechaCreacion") val fechaCreacion: String
)

data class ResumenReportesDto(
    @SerializedName("total")      val total: Int,
    @SerializedName("pendientes") val pendientes: Int,
    @SerializedName("enProceso")  val enProceso: Int,
    @SerializedName("resueltos")  val resueltos: Int
)
