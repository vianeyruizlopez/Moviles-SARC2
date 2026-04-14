package com.williamsel.sarc.features.superadmin.panelsuperadmin.data.models

import com.google.gson.annotations.SerializedName

data class PanelSuperAdminDto(
    @SerializedName("nombre")           val nombre: String,
    @SerializedName("primer_apellido")  val primerApellido: String,
    @SerializedName("segundo_apellido") val segundoApellido: String,
    @SerializedName("total_reportes")   val totalReportes: Int,
    @SerializedName("pendientes")       val pendientes: Int,
    @SerializedName("en_proceso")       val enProceso: Int,
    @SerializedName("resueltos")        val resueltos: Int,
    @SerializedName("total_admins")     val totalAdmins: Int,
    @SerializedName("total_ciudadanos") val totalCiudadanos: Int
)
