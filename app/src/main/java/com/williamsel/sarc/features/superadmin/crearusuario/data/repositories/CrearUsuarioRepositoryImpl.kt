package com.williamsel.sarc.features.superadmin.crearusuario.data.repositories

import com.williamsel.sarc.core.database.dao.UsuarioDao
import com.williamsel.sarc.features.superadmin.crearusuario.data.datasource.api.CrearUsuarioApi
import com.williamsel.sarc.features.superadmin.crearusuario.data.mapper.toAdminDomain
import com.williamsel.sarc.features.superadmin.crearusuario.data.mapper.toEntity
import com.williamsel.sarc.features.superadmin.crearusuario.data.mapper.toRequest
import com.williamsel.sarc.features.superadmin.crearusuario.data.mapper.toDomain
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.Administrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.NuevoAdministrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.repositories.CrearUsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CrearUsuarioRepositoryImpl @Inject constructor(
    private val api: CrearUsuarioApi,
    private val usuarioDao: UsuarioDao
) : CrearUsuarioRepository {

    override suspend fun crearAdministrador(nuevo: NuevoAdministrador): Result<Int> {
        return try {
            // 1. Guardar en Room primero (offline-first)
            usuarioDao.insert(nuevo.toEntity())
            // 2. Enviar a la API
            val response = api.crearAdministrador(nuevo.toRequest())
            Result.success(response.idUsuario ?: 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Flow reactivo desde Room — idRol == 2 filtra solo administradores
    override fun getAdministradores(): Flow<List<Administrador>> =
        usuarioDao.getAll().map { entities ->
            entities
                .filter { it.idRol == 2 }
                .map { it.toAdminDomain() }
        }

    override suspend fun toggleActivo(idUsuario: Int, activo: Boolean): Result<Unit> {
        // toggleActivo was removed from the API — no-op
        return Result.failure(UnsupportedOperationException("toggleActivo is no longer supported by the API"))
    }

    override suspend fun eliminarAdministrador(idUsuario: Int): Result<Unit> {
        return try {
            api.eliminarAdministrador(idUsuario)
            usuarioDao.deleteById(idUsuario)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
