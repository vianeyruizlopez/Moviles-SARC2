package com.williamsel.sarc.features.administrador.mapaadmin.data.mapper

import com.williamsel.sarc.features.administrador.mapaadmin.data.models.MapaReporteDto
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte

fun MapaReporteDto.toDomain(): MapaReporte = MapaReporte(
    idReporte     = idReporte,
    idUsuario     = idUsuario,
    nombreUsuario = nombreUsuario ?: "Usuario anónimo",
    titulo        = titulo ?: "Sin título",
    descripcion   = descripcion ?: "Sin descripción",
    ubicacion     = ubicacion ?: "Ubicación desconocida",
    latitud       = latitud,
    longitud      = longitud,
    incidencia    = CategoriaIncidencia.fromNombre(nombreIncidencia ?: ""),
    estado        = EstadoMapaReporte.fromNombre(nombreEstado ?: ""),
    imagen        = imagen,
    fecha         = fecha ?: "Fecha no disponible"
)