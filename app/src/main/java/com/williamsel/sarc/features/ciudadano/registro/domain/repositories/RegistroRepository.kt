package com.williamsel.sarc.features.publico.registro.domain.repositories

import com.williamsel.sarc.features.publico.registro.domain.entities.UsuarioRegistrado

interface RegistroRepository {
    suspend fun registrar(nombre: String, correo: String, contrasena: String): UsuarioRegistrado?
    suspend fun registrarConGoogle(idToken: String): UsuarioRegistrado?
}
