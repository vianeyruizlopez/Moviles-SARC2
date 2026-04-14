package com.williamsel.sarc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    foreignKeys = [
        ForeignKey(
            entity = RolEntity::class,
            parentColumns = ["id_rol"],
            childColumns = ["id_rol"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["google_id"], unique = true),
        Index("id_rol")
    ]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int = 0,

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

    /** Autenticación con Google OAuth */
    @ColumnInfo(name = "google_id")
    val googleId: String? = null,

    /** Debe ser >= 18 — validar en la capa de negocio antes de insertar */
    @ColumnInfo(name = "edad")
    val edad: Int? = null,

    @ColumnInfo(name = "id_rol")
    val idRol: Int? = null
)
