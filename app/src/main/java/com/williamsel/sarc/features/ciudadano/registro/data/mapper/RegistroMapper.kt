package com.williamsel.sarc.features.ciudadano.registro.data.mapper

import com.williamsel.sarc.features.ciudadano.registro.data.models.RegistroDto
import com.williamsel.sarc.features.ciudadano.registro.domain.entities.UsuarioRegistrado

fun RegistroDto.toDomain(): UsuarioRegistrado = UsuarioRegistrado(
    id = id,
    nombre = nombre,
    primerApellido = primerApellido,
    segundoApellido = segundoApellido,
    correo = email,
    edad = edad,
    token = "",
    rol = nombreRol,
    googleId = googleId
)
