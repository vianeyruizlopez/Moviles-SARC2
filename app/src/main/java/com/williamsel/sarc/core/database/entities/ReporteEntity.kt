package com.williamsel.sarc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reporte",
    indices = [
        Index("id_usuario"),
        Index("id_incidencia"),
        Index("id_estado")
    ]
)
data class ReporteEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_reporte")
    val idReporte: Int,

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "descripcion")
    val descripcion: String,

    @ColumnInfo(name = "imagen")
    val imagen: String? = null,

    @ColumnInfo(name = "latitud")
    val latitud: Double,

    @ColumnInfo(name = "longitud")
    val longitud: Double,

    @ColumnInfo(name = "fecha_reporte")
    val fechaReporte: String? = null,

    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int? = null,

    @ColumnInfo(name = "id_incidencia")
    val idIncidencia: Int? = null,

    @ColumnInfo(name = "id_estado")
    val idEstado: Int? = 1,

    @ColumnInfo(name = "ubicacion")
    val ubicacion: String? = null
)
