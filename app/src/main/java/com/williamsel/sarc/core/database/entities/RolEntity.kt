package com.williamsel.sarc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rol")
data class RolEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_rol")
    val idRol: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String
)
