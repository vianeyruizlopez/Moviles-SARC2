package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.models

import com.google.gson.annotations.SerializedName

data class ResumenReportesDto(
    @SerializedName("total")      val total: Int,
    @SerializedName("pendientes") val pendientes: Int,
    @SerializedName("enProceso")  val enProceso: Int,
    @SerializedName("resueltos")  val resueltos: Int
)
