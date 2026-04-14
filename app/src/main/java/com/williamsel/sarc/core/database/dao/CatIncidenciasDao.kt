package com.williamsel.sarc.core.database.dao

import androidx.room.*
import com.williamsel.sarc.core.database.entities.CatIncidenciasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatIncidenciasDao {

    @Query("SELECT * FROM cat_incidencias")
    fun getAll(): Flow<List<CatIncidenciasEntity>>

    @Query("SELECT * FROM cat_incidencias WHERE id_incidencia = :id")
    suspend fun getById(id: Int): CatIncidenciasEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(catIncidencias: CatIncidenciasEntity): Long

    @Update
    suspend fun update(catIncidencias: CatIncidenciasEntity)

    @Delete
    suspend fun delete(catIncidencias: CatIncidenciasEntity)
}
