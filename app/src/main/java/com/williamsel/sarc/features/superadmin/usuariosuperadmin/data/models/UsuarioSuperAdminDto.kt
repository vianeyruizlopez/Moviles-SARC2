package com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.models

import com.google.gson.annotations.SerializedName

data class UsuarioSistemaDto(
    @SerializedName("id_usuario")       val idUsuario: Int,
    @SerializedName("nombre")           val nombre: String,
    @SerializedName("primer_apellido")  val primerApellido: String,
    @SerializedName("segundo_apellido") val segundoApellido: String,
    @SerializedName("email")            val email: String,
    @SerializedName("id_rol")           val idRol: Int,
    @SerializedName("activo")           val activo: Boolean
)

data class ToggleActivoRequest(
    @SerializedName("activo") val activo: Boolean
)
