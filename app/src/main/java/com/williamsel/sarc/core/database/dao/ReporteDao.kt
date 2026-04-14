package com.williamsel.sarc.core.database.dao

import androidx.room.*
import com.williamsel.sarc.core.database.entities.ReporteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Query("SELECT * FROM reporte ORDER BY fecha_reporte DESC")
    fun getAll(): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reporte WHERE id_reporte = :id")
    suspend fun getById(id: Int): ReporteEntity?

    @Query("SELECT * FROM reporte WHERE id_usuario = :idUsuario ORDER BY fecha_reporte DESC")
    fun getByUsuario(idUsuario: Int): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reporte WHERE id_estado = :idEstado ORDER BY fecha_reporte DESC")
    fun getByEstado(idEstado: Int): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reporte WHERE id_incidencia = :idIncidencia ORDER BY fecha_reporte DESC")
    fun getByIncidencia(idIncidencia: Int): Flow<List<ReporteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reporte: ReporteEntity): Long

    @Update
    suspend fun update(reporte: ReporteEntity)

    @Delete
    suspend fun delete(reporte: ReporteEntity)

    @Query("DELETE FROM reporte WHERE id_reporte = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE reporte SET id_estado = :idEstado WHERE id_reporte = :idReporte")
    suspend fun updateEstado(idReporte: Int, idEstado: Int)
}
