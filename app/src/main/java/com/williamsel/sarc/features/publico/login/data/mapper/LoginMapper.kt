package com.williamsel.sarc.features.publico.login.data.mapper

import com.williamsel.sarc.core.utils.JwtDecoder
import com.williamsel.sarc.features.publico.login.data.models.LoginDto
import com.williamsel.sarc.features.publico.login.domain.entities.Login

fun LoginDto.toDomain(): Login {
    val idUsuario = JwtDecoder.getUserId(token)
    val rolName = JwtDecoder.getRolName(token)

    return Login(
        id     = idUsuario,
        correo = "",
        token  = token,
        rol    = rolName
    )
}
