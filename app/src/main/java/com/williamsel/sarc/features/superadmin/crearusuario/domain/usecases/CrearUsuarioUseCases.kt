package com.williamsel.sarc.features.superadmin.crearusuario.domain.usecases

import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.Administrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.NuevoAdministrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.repositories.CrearUsuarioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CrearAdministradorUseCase @Inject constructor(
    private val repository: CrearUsuarioRepository
) {
    suspend operator fun invoke(nuevo: NuevoAdministrador): Result<Int> =
        repository.crearAdministrador(nuevo)
}

class GetAdministradoresUseCase @Inject constructor(
    private val repository: CrearUsuarioRepository
) {
    operator fun invoke(): Flow<List<Administrador>> =
        repository.getAdministradores()
}

class ToggleActivoUseCase @Inject constructor(
    private val repository: CrearUsuarioRepository
) {
    suspend operator fun invoke(idUsuario: Int, activo: Boolean): Result<Unit> =
        repository.toggleActivo(idUsuario, activo)
}

class EliminarAdministradorUseCase @Inject constructor(
    private val repository: CrearUsuarioRepository
) {
    suspend operator fun invoke(idUsuario: Int): Result<Unit> =
        repository.eliminarAdministrador(idUsuario)
}
