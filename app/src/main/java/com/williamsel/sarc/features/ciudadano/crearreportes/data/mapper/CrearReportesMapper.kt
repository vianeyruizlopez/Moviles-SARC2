package com.williamsel.sarc.features.ciudadano.crearreportes.data.mapper

import com.williamsel.sarc.features.ciudadano.crearreportes.data.models.CategoriaDto
import com.williamsel.sarc.features.ciudadano.crearreportes.data.models.CrearReporteRequest
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.NuevoReporte

fun NuevoReporte.toRequest(idUsuario: Int): CrearReporteRequest = CrearReporteRequest(
    titulo        = titulo,
    descripcion   = descripcion,
    ubicacion     = ubicacion,
    latitud       = latitud,
    longitud      = longitud,
    idUsuario     = idUsuario,
    idIncidencias = idIncidencia,
    idEstado      = 1
)

fun CategoriaDto.toDomain(): CategoriaIncidencia = CategoriaIncidencia(
    id     = id,
    nombre = nombre,
    emoji  = when (nombre.lowercase()) {
        "bache"       -> "🚧"
        "basura"      -> "🗑️"
        "alumbrado"   -> "💡"
        else          -> "📋"
    }
)
