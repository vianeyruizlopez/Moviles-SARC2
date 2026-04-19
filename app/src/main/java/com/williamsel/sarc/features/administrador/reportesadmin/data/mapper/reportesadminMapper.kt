package com.williamsel.sarc.features.administrador.reportesadmin.data.mapper

import com.williamsel.sarc.features.administrador.reportesadmin.data.models.ReporteAdminDto
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin

fun ReporteAdminDto.toDomain(): ReporteAdmin {
    return ReporteAdmin(
        idReporte        = idReporte,
        idUsuario        = idUsuario,
        nombreUsuario    = nombreUsuario.cleanQuotes(),
        nombreIncidencia = nombreIncidencia,
        nombreEstado     = nombreEstado,
        idIncidencia     = idIncidencia,
        titulo           = titulo.cleanQuotes(),
        descripcion      = descripcion.cleanQuotes(),
        ubicacion        = ubicacion,
        latitud          = latitud,
        longitud         = longitud,
        idEstado         = idEstado,
        imagen           = imagen,
        fecha            = fecha
    )
}

private fun String.cleanQuotes(): String {
    return this.replace("\"", "").trim()
}
