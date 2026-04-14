package com.williamsel.sarc.features.ciudadano.misreportesciu.data.mapper

import com.williamsel.sarc.core.database.entities.ReporteEntity
import com.williamsel.sarc.features.ciudadano.misreportesciu.data.models.ReporteCiudadanoDto
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
fun ReporteCiudadanoDto.toDomain(): ReporteCiudadano = ReporteCiudadano(
    idReporte = idReportes,
    titulo = titulo,
    descripcion = descripcion,
    ubicacion = ubicacion,
    fechaReporte = fechaReporte,
    incidencia = incidencia,
    estado = estado,
    idEstado = idEstado,
    puedeEditar = idEstado == 1
)

fun ReporteEntity.toDomain(
    nombreIncidencia: String = "Otro",
    nombreEstado: String = when (idEstado) {
        1    -> "Pendiente"
        2    -> "En Proceso"
        3    -> "Resuelto"
        else -> "Desconocido"
    }
): ReporteCiudadano = ReporteCiudadano(
    idReporte = idReporte,
    titulo = titulo,
    descripcion = descripcion,
    ubicacion = ubicacion ?: "",
    fechaReporte = fechaReporte ?: 0L,
    incidencia = nombreIncidencia,
    estado = nombreEstado,
    idEstado = idEstado ?: 1,
    puedeEditar = idEstado == 1
)
