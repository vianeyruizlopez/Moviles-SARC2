package com.williamsel.sarc.features.ciudadano.registro.data.repositories

import com.williamsel.sarc.features.publico.login.data.datasource.GoogleAuthUiClient
import com.williamsel.sarc.features.ciudadano.registro.data.datasource.api.RegistroApi
import com.williamsel.sarc.features.ciudadano.registro.data.mapper.toDomain
import com.williamsel.sarc.features.ciudadano.registro.data.models.RegistroGoogleRequestDto
import com.williamsel.sarc.features.ciudadano.registro.data.models.RegistroRequestDto
import com.williamsel.sarc.features.ciudadano.registro.domain.entities.UsuarioRegistrado
import com.williamsel.sarc.features.ciudadano.registro.domain.repositories.RegistroRepository
import javax.inject.Inject

class RegistroRepositoryImpl @Inject constructor(
    private val api: RegistroApi,
    private val googleAuthUiClient: GoogleAuthUiClient
) : RegistroRepository {

    override suspend fun registrar(
        nombre: String,
        primerApellido: String,
        segundoApellido: String,
        correo: String,
        contrasena: String,
        edad: Int
    ): UsuarioRegistrado? {
        return try {
            api.registrar(
                RegistroRequestDto(
                    nombre = nombre,
                    primerApellido = primerApellido,
                    segundoApellido = segundoApellido,
                    email = correo,
                    contrasena = contrasena,
                    edad = edad
                )
            ).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun registrarConGoogle(idToken: String): UsuarioRegistrado? {
        return try {
            api.registrarConGoogle(RegistroGoogleRequestDto(idToken)).toDomain()
        } catch (e: Exception) {
            null
        }
    }
}
