package com.williamsel.sarc.features.ciudadano.registro.domain.usecases

import com.williamsel.sarc.features.ciudadano.registro.domain.entities.UsuarioRegistrado
import com.williamsel.sarc.features.ciudadano.registro.domain.repositories.RegistroRepository
import javax.inject.Inject

class RegistrarUseCase @Inject constructor(
    private val repository: RegistroRepository
) {
    suspend operator fun invoke(
        nombre: String,
        primerApellido: String,
        segundoApellido: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String,
        edad: String
    ): Result<UsuarioRegistrado> {
        if (nombre.isBlank()) return Result.failure(Exception("El nombre es obligatorio"))
        if (primerApellido.isBlank()) return Result.failure(Exception("El primer apellido es obligatorio"))
        if (correo.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            return Result.failure(Exception("Correo electrónico no válido"))
        }
        
        val edadInt = edad.toIntOrNull()
        if (edadInt == null || edadInt < 18) {
            return Result.failure(Exception("Debes ser mayor de 18 años"))
        }

        if (contrasena.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        if (contrasena != confirmarContrasena) {
            return Result.failure(Exception("Las contraseñas no coinciden"))
        }

        return try {
            val usuario = repository.registrar(
                nombre, primerApellido, segundoApellido, correo, contrasena, edadInt
            )
            if (usuario != null) Result.success(usuario)
            else Result.failure(Exception("Error al registrar usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class RegistrarConGoogleUseCase @Inject constructor(
    private val repository: RegistroRepository
) {
    suspend operator fun invoke(idToken: String): UsuarioRegistrado? =
        repository.registrarConGoogle(idToken)
}