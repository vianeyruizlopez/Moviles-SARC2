package com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.mapper

import com.williamsel.sarc.core.database.entities.UsuarioEntity
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.data.models.UsuarioSistemaDto
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.RolUsuario
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.UsuarioSistema

fun UsuarioSistemaDto.toDomain() = UsuarioSistema(
    idUsuario      = idUsuario,
    nombreCompleto = "$nombre $primerApellido $segundoApellido".trim(),
    email          = email,
    rol            = when (idRol) {
        3    -> RolUsuario.SUPER_ADMIN
        2    -> RolUsuario.ADMIN
        else -> RolUsuario.CIUDADANO
    },
    activo = activo
)

// Room Entity → Domain (fallback sin red, activo = true por defecto)
fun UsuarioEntity.toDomain() = UsuarioSistema(
    idUsuario      = idUsuario,
    nombreCompleto = "$nombre $primerApellido $segundoApellido".trim(),
    email          = email,
    rol            = when (idRol) {
        3    -> RolUsuario.SUPER_ADMIN
        2    -> RolUsuario.ADMIN
        else -> RolUsuario.CIUDADANO
    },
    activo = true
)
