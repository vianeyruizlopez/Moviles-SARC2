package com.williamsel.sarc.features.ciudadano.registro.domain.repositories

import com.williamsel.sarc.features.ciudadano.registro.domain.entities.UsuarioRegistrado

interface RegistroRepository {
    suspend fun registrar(
        nombre: String,
        primerApellido: String,
        segundoApellido: String,
        correo: String,
        contrasena: String,
        edad: Int
    ): UsuarioRegistrado?

    suspend fun registrarConGoogle(idToken: String): UsuarioRegistrado?
}