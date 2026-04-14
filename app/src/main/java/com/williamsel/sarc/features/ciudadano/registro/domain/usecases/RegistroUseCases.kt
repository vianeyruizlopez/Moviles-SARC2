package com.williamsel.sarc.features.publico.registro.domain.usecases

import com.williamsel.sarc.features.publico.registro.domain.entities.UsuarioRegistrado
import com.williamsel.sarc.features.publico.registro.domain.repositories.RegistroRepository
import javax.inject.Inject

class RegistrarUseCase @Inject constructor(
    private val repository: RegistroRepository
) {
    suspend operator fun invoke(
        nombre: String,
        correo: String,
        contrasena: String
    ): UsuarioRegistrado? = repository.registrar(nombre, correo, contrasena)
}

class RegistrarConGoogleUseCase @Inject constructor(
    private val repository: RegistroRepository
) {
    suspend operator fun invoke(idToken: String): UsuarioRegistrado? =
        repository.registrarConGoogle(idToken)
}
