package com.williamsel.sarc.features.administrador.mapaadmin.data.mapper

import com.williamsel.sarc.features.administrador.mapaadmin.data.models.MapaReporteDto
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte

fun MapaReporteDto.toDomain(): MapaReporte = MapaReporte(
    idReporte     = idReporte,
    idUsuario     = idUsuario,
    nombreUsuario = nombreUsuario,
    titulo        = titulo.trim('"'),
    descripcion   = descripcion.trim('"'),
    ubicacion     = ubicacion,
    latitud       = latitud,
    longitud      = longitud,
    incidencia    = CategoriaIncidencia.fromNombre(nombreIncidencia),
    estado        = EstadoMapaReporte.fromNombre(nombreEstado),
    imagen        = imagen,
    fecha         = fecha
)
