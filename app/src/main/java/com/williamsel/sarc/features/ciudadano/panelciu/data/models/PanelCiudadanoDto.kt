package com.williamsel.sarc.features.ciudadano.panelciu.data.models

import com.google.gson.annotations.SerializedName

data class PanelCiudadanoDto(
    @SerializedName("total")      val total: Int,
    @SerializedName("pendientes") val pendientes: Int,
    @SerializedName("enProceso")  val enProceso: Int,
    @SerializedName("resueltos")   val resueltos: Int
)
