package com.williamsel.sarc.features.administrador.paneldeadmin.data.mapper

import com.williamsel.sarc.features.administrador.paneldeadmin.data.models.PanelReporteDto
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte

fun PanelReporteDto.toDomain(): PanelReporte = PanelReporte(
    idReporte        = idReporte,
    idUsuario        = idUsuario,
    nombreUsuario    = nombreUsuario,
    nombreIncidencia = nombreIncidencia,
    nombreEstado     = nombreEstado,
    idIncidencia     = idIncidencia,
    titulo           = titulo.trim('"'),
    descripcion      = descripcion.trim('"'),
    ubicacion        = ubicacion,
    latitud          = latitud,
    longitud         = longitud,
    idEstado         = idEstado,
    imagen           = imagen,
    fecha            = fecha
)
