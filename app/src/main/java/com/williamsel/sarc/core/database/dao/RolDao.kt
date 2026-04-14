package com.williamsel.sarc.core.database.dao

import androidx.room.*
import com.williamsel.sarc.core.database.entities.RolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RolDao {

    @Query("SELECT * FROM rol")
    fun getAll(): Flow<List<RolEntity>>

    @Query("SELECT * FROM rol WHERE id_rol = :id")
    suspend fun getById(id: Int): RolEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rol: RolEntity): Long

    @Update
    suspend fun update(rol: RolEntity)

    @Delete
    suspend fun delete(rol: RolEntity)
}
