package com.williamsel.sarc.features.ciudadano.mapaciu.data.mapper

import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.ReporteMapaDto
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa

fun ReporteMapaDto.toDomain(): ReporteMapa = ReporteMapa(
    idReporte    = idReporte,
    nombre       = nombre,
    descripcion  = descripcion,
    latitud      = latitud,
    longitud     = longitud,
    idIncidencia = idIncidencia,
    idEstado     = idEstado,
    fechaReporte = fechaReporte
)
