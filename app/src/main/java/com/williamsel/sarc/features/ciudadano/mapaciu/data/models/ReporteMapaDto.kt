package com.williamsel.sarc.features.ciudadano.mapaciu.data.models

import com.google.gson.annotations.SerializedName

data class ReporteMapaDto(
    @SerializedName("idReporte")
    val idReporte:    Int,
    
    @SerializedName("titulo")
    val nombre:       String,
    
    @SerializedName("latitud")
    val latitud:      Double,
    
    @SerializedName("longitud")
    val longitud:     Double,
    
    @SerializedName("idIncidencia")
    val idIncidencia: Int?,
    
    @SerializedName("idEstado")
    val idEstado:     Int?,

    @SerializedName("descripcion")
    val descripcion:  String? = null,
    
    @SerializedName("fechaReporte")
    val fechaReporte: Long? = null
)
