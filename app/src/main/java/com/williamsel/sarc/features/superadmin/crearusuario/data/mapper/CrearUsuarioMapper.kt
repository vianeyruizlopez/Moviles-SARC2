package com.williamsel.sarc.features.superadmin.crearusuario.data.mapper

import com.williamsel.sarc.core.database.entities.UsuarioEntity
import com.williamsel.sarc.features.superadmin.crearusuario.data.models.AdministradorDto
import com.williamsel.sarc.features.superadmin.crearusuario.data.models.CrearAdminRequest
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.Administrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.NuevoAdministrador

fun NuevoAdministrador.toRequest(): CrearAdminRequest = CrearAdminRequest(
    nombre          = nombre,
    primerApellido  = primerApellido,
    segundoApellido = segundoApellido,
    email           = email,
    contrasena      = contrasena,
    idRol           = 2
)

fun AdministradorDto.toDomain(): Administrador = Administrador(
    idUsuario      = idUsuario,
    nombreCompleto = "$nombre $primerApellido $segundoApellido".trim(),
    email          = email,
    activo         = activo
)

// Room
fun UsuarioEntity.toAdminDomain(): Administrador = Administrador(
    idUsuario      = idUsuario,
    nombreCompleto = "$nombre $primerApellido $segundoApellido".trim(),
    email          = email,
    activo         = true
)

fun NuevoAdministrador.toEntity(): UsuarioEntity = UsuarioEntity(
    idUsuario       = 0,
    nombre          = nombre,
    primerApellido  = primerApellido,
    segundoApellido = segundoApellido,
    email           = email,
    edad            = null,
    idRol           = 2
)
