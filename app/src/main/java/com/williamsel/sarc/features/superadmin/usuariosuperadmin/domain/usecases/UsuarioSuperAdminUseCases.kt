package com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.usecases

import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.ResumenUsuarios
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.repositories.UsuarioSuperAdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetResumenUsuariosUseCase @Inject constructor(
    private val repository: UsuarioSuperAdminRepository
) {
    operator fun invoke(): Flow<ResumenUsuarios> =
        repository.getResumenUsuarios()
}

class ToggleActivoUsuarioUseCase @Inject constructor(
    private val repository: UsuarioSuperAdminRepository
) {
    suspend operator fun invoke(idUsuario: Int, activo: Boolean): Result<Unit> =
        repository.toggleActivoUsuario(idUsuario, activo)
}
