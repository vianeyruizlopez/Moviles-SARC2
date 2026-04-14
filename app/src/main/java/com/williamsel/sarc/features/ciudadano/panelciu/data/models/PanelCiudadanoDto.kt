package com.williamsel.sarc.features.ciudadano.panelciu.data.models

import com.google.gson.annotations.SerializedName

data class PanelCiudadanoDto(
    @SerializedName("id_usuario")    val idUsuario: Int,
    @SerializedName("nombre")        val nombre: String,
    @SerializedName("primer_apellido") val primerApellido: String,
    @SerializedName("segundo_apellido") val segundoApellido: String,
    @SerializedName("total")         val total: Int,
    @SerializedName("pendientes")    val pendientes: Int,
    @SerializedName("en_proceso")    val enProceso: Int,
    @SerializedName("resueltos")     val resueltos: Int
)
