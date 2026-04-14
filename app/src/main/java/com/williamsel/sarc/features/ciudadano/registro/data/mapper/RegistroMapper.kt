package com.williamsel.sarc.features.publico.registro.data.mapper

import com.williamsel.sarc.features.publico.registro.data.models.RegistroDto
import com.williamsel.sarc.features.publico.registro.domain.entities.UsuarioRegistrado

fun RegistroDto.toDomain(): UsuarioRegistrado = UsuarioRegistrado(
    id       = id,
    nombre   = nombre,
    correo   = correo,
    token    = token,
    rol      = rol,
    googleId = googleId
)
