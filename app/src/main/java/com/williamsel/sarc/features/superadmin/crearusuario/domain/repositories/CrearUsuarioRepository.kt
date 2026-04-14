package com.williamsel.sarc.features.superadmin.crearusuario.domain.repositories

import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.Administrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.NuevoAdministrador
import kotlinx.coroutines.flow.Flow

interface CrearUsuarioRepository {
    suspend fun crearAdministrador(nuevo: NuevoAdministrador): Result<Int>
    fun getAdministradores(): Flow<List<Administrador>>
    suspend fun toggleActivo(idUsuario: Int, activo: Boolean): Result<Unit>
    suspend fun eliminarAdministrador(idUsuario: Int): Result<Unit>
}
