package com.williamsel.sarc.features.perfil.data.mapper

import com.williamsel.sarc.core.database.entities.UsuarioEntity
import com.williamsel.sarc.features.perfil.data.models.PerfilDto
import com.williamsel.sarc.features.perfil.domain.entities.Usuario

//rooms
fun UsuarioEntity.toDomain(): Usuario {
    return Usuario(
        id = this.idUsuario,
        nombreCompleto = "${this.nombre} ${this.primerApellido} ${this.segundoApellido ?: ""}".trim(),
        email = this.email,
        rol = this.idRol ?: 3
    )
}

//api de aws
fun PerfilDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        idUsuario = this.id,
        nombre = this.nombre,
        primerApellido = this.primerApellido,
        segundoApellido = this.segundoApellido,
        email = this.email,
        edad = this.edad,
        idRol = this.idRol
    )
}
