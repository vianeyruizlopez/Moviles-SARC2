package com.williamsel.sarc.features.superadmin.crearusuario.data.models

import com.google.gson.annotations.SerializedName

data class CrearAdminRequest(
    @SerializedName("nombre")           val nombre: String,
    @SerializedName("primer_apellido")  val primerApellido: String,
    @SerializedName("segundo_apellido") val segundoApellido: String,
    @SerializedName("email")            val email: String,
    @SerializedName("contrasena")       val contrasena: String,
    @SerializedName("id_rol")           val idRol: Int = 2   // 2 = ADMIN
)

data class AdministradorDto(
    @SerializedName("id_usuario")       val idUsuario: Int,
    @SerializedName("nombre")           val nombre: String,
    @SerializedName("primer_apellido")  val primerApellido: String,
    @SerializedName("segundo_apellido") val segundoApellido: String,
    @SerializedName("email")            val email: String,
    @SerializedName("activo")           val activo: Boolean
)

data class ToggleActivoRequest(
    @SerializedName("activo") val activo: Boolean
)

data class MensajeResponse(
    @SerializedName("id_usuario") val idUsuario: Int? = null,
    @SerializedName("mensaje")    val mensaje: String
)
