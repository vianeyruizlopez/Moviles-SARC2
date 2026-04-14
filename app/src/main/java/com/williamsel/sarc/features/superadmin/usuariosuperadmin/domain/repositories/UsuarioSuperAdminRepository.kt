package com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.repositories

import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.ResumenUsuarios
import kotlinx.coroutines.flow.Flow

interface UsuarioSuperAdminRepository {
    fun getResumenUsuarios(): Flow<ResumenUsuarios>
    suspend fun toggleActivoUsuario(idUsuario: Int, activo: Boolean): Result<Unit>
}
