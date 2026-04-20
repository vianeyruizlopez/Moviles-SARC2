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
            onDelete = ForeignKey.SET_NULL // Si borran el rol, el usuario no se borra, solo queda en null
        )
    ],
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
    val segundoApellido: String?,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "edad")
    val edad: Int? = null,

    @ColumnInfo(name = "id_rol")
    val idRol: Int? = null
)