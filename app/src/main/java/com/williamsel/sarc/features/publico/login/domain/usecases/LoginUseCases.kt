package com.williamsel.sarc.features.publico.login.domain.usecases

import com.williamsel.sarc.features.publico.login.domain.entities.Login
import com.williamsel.sarc.features.publico.login.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(correo: String, contrasena: String): Login? =
        repository.login(correo, contrasena)
}

class LoginConGoogleUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(idToken: String): Login? =
        repository.loginConGoogle(idToken)
}

class RestaurarSesionUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(): Login? {
        if (!repository.isUserSignedIn()) return null
        return repository.restaurarSesion()
    }
}

class SignOutUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke() = repository.signOut()
}
