package com.williamsel.sarc.features.ciudadano.detallereporteciu.data.mapper

import com.williamsel.sarc.features.ciudadano.detallereporteciu.data.models.DetallereporteciuDto
import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.entities.Detallereporteciu

fun DetallereporteciuDto.toDomain(): Detallereporteciu = Detallereporteciu(
    id          = id,
    titulo      = titulo,
    descripcion = descripcion,
    categoria   = categoria,
    iconoUrl    = when(categoria.lowercase()) {
        "bache" -> "🚧"
        "basura" -> "🗑️"
        "luminaria" -> "💡"
        else -> null
    },
    imagenUrl   = imagenUrl,
    estado      = estado,
    direccion   = direccion,
    latitud     = latitud,
    longitud    = longitud,
    fecha       = fecha
)
