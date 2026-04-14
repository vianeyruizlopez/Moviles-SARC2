package com.williamsel.sarc.features.publico.login.data.mapper

import android.util.Base64
import com.williamsel.sarc.features.publico.login.data.models.LoginDto
import com.williamsel.sarc.features.publico.login.domain.entities.Login
import org.json.JSONObject

fun LoginDto.toDomain(): Login {
    val payload = decodeJwtPayload(token)
    val id = payload.optInt("id", 0)
    val idRol = payload.optInt("idRol", 1)

    val rol = when (idRol) {
        1 -> "Administrador"
        2 -> "Ciudadano"
        3 -> "SuperAdministrador"
        else -> "Ciudadano"
    }

    return Login(
        id     = id,
        correo = "",
        token  = token,
        rol    = rol
    )
}

private fun decodeJwtPayload(jwt: String): JSONObject {
    return try {
        val parts = jwt.split(".")
        if (parts.size < 2) return JSONObject()
        val decoded = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING)
        JSONObject(String(decoded))
    } catch (e: Exception) {
        JSONObject()
    }
}
