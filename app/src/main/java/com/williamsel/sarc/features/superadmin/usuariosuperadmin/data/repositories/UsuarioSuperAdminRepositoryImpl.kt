package com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.repositories

import com.williamsel.sarc.core.database.dao.UsuarioDao
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.datasource.api.UsuarioSuperAdminApi
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.mapper.toDomain
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.ResumenUsuarios
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.RolUsuario
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.repositories.UsuarioSuperAdminRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioSuperAdminRepositoryImpl @Inject constructor(
    private val api: UsuarioSuperAdminApi,
    private val usuarioDao: UsuarioDao
) : UsuarioSuperAdminRepository {

    // Flow reactivo desde Room — se actualiza al insertar/eliminar usuarios
    override fun getResumenUsuarios(): Flow<ResumenUsuarios> =
        usuarioDao.getAll().map { entities ->
            val usuarios = entities.map { it.toDomain() }
            val admins   = usuarios.filter {
                it.rol == RolUsuario.ADMIN || it.rol == RolUsuario.SUPER_ADMIN
            }
            val ciudadanos = usuarios.filter { it.rol == RolUsuario.CIUDADANO }

            ResumenUsuarios(
                totalUsuarios        = usuarios.size,
                totalAdministradores = admins.size,
                totalCiudadanos      = ciudadanos.size,
                administradores      = admins,
                ciudadanos           = ciudadanos
            )
        }

    override suspend fun toggleActivoUsuario(idUsuario: Int, activo: Boolean): Result<Unit> {
        // toggleActivo was removed from the API — no-op
        return Result.failure(UnsupportedOperationException("toggleActivo is no longer supported by the API"))
    }
}
