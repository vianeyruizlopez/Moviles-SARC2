package com.williamsel.sarc.core.database.dao

import androidx.room.*
import com.williamsel.sarc.core.database.entities.CatEstadoReporteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatEstadoReporteDao {

    @Query("SELECT * FROM cat_estado_reporte")
    fun getAll(): Flow<List<CatEstadoReporteEntity>>

    @Query("SELECT * FROM cat_estado_reporte WHERE id_estado = :id")
    suspend fun getById(id: Int): CatEstadoReporteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estado: CatEstadoReporteEntity): Long

    @Update
    suspend fun update(estado: CatEstadoReporteEntity)

    @Delete
    suspend fun delete(estado: CatEstadoReporteEntity)
}
