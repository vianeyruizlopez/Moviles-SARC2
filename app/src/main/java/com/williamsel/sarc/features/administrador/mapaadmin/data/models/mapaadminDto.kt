package com.williamsel.sarc.features.administrador.mapaadmin.data.models

import com.google.gson.annotations.SerializedName

data class MapaReporteDto(
    @SerializedName("id_reporte")       val idReporte: Int,
    @SerializedName("id_usuario")       val idUsuario: Int,
    @SerializedName("nombreUsuario")    val nombreUsuario: String?,
    @SerializedName("nombreIncidencia") val nombreIncidencia: String?,
    @SerializedName("nombreEstado")     val nombreEstado: String?,
    @SerializedName("id_incidencia")    val idIncidencia: Int,
    @SerializedName("titulo")           val titulo: String?,
    @SerializedName("descripcion")      val descripcion: String?,
    @SerializedName("ubicacion")        val ubicacion: String?,
    @SerializedName("latitud")          val latitud: Double,
    @SerializedName("longitud")         val longitud: Double,
    @SerializedName("id_estado")        val idEstado: Int,
    @SerializedName("imagen")           val imagen: String?,
    @SerializedName("fecha")            val fecha: String? 
)