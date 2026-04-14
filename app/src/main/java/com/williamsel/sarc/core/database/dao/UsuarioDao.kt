package com.williamsel.sarc.core.database.dao

import androidx.room.*
import com.williamsel.sarc.core.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario")
    fun getAll(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuario WHERE id_usuario = :id")
    suspend fun getById(id: Int): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE google_id = :googleId LIMIT 1")
    suspend fun getByGoogleId(googleId: String): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE email = :email AND contrasena = :contrasena LIMIT 1")
    suspend fun login(email: String, contrasena: String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity): Long

    @Update
    suspend fun update(usuario: UsuarioEntity)

    @Delete
    suspend fun delete(usuario: UsuarioEntity)

    @Query("DELETE FROM usuario WHERE id_usuario = :id")
    suspend fun deleteById(id: Int)
}
