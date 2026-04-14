package com.williamsel.sarc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reporte",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = CatIncidenciasEntity::class,
            parentColumns = ["id_incidencia"],
            childColumns = ["id_incidencia"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = CatEstadoReporteEntity::class,
            parentColumns = ["id_estado"],
            childColumns = ["id_estado"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("id_usuario"),
        Index("id_incidencia"),
        Index("id_estado")
    ]
)
data class ReporteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_reporte")
    val idReporte: Int = 0,

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
    val fechaReporte: Long? = System.currentTimeMillis(),

    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int? = null,

    @ColumnInfo(name = "id_incidencia")
    val idIncidencia: Int? = null,

    @ColumnInfo(name = "id_estado")
    val idEstado: Int? = 1,

    @ColumnInfo(name = "ubicacion")
    val ubicacion: String? = null
)
