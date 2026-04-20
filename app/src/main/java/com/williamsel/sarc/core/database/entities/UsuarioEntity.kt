package com.williamsel.sarc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    indices = [
        Index(value = ["email"], unique = true),
        Index("id_rol")
    ]
)
data class UsuarioEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "primer_apellido")
    val primerApellido: String,

    @ColumnInfo(name = "segundo_apellido")
    val segundoApellido: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "contrasena")
    val contrasena: String? = null,

    @ColumnInfo(name = "google_id")
    val googleId: String? = null,

    @ColumnInfo(name = "edad")
    val edad: Int? = null,

    @ColumnInfo(name = "id_rol")
    val idRol: Int? = null
)
