package com.williamsel.sarc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_estado_reporte")
data class CatEstadoReporteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_estado")
    val idEstado: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String
)
