package com.williamsel.sarc.features.administrador.detallereporteadmin.data.mapper

import com.williamsel.sarc.features.administrador.detallereporteadmin.data.models.DetalleReporteAdminDto
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin

fun DetalleReporteAdminDto.toDomain(): DetalleReporteAdmin {
    return DetalleReporteAdmin(
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
    return this.removeSurrounding("\"")
}
