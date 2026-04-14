package com.williamsel.sarc.features.administrador.paneladmin.data.mapper

import com.williamsel.sarc.features.administrador.paneladmin.data.models.ReporteDto
import com.williamsel.sarc.features.administrador.paneladmin.data.models.ResumenReportesDto
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.EstadoReporte
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.Reporte
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes

fun ReporteDto.toDomain(): Reporte = Reporte(
    id           = id,
    titulo       = titulo,
    descripcion  = descripcion,
    estado       = when (estado.uppercase()) {
        "PENDIENTE"  -> EstadoReporte.PENDIENTE
        "EN_PROCESO" -> EstadoReporte.EN_PROCESO
        "RESUELTO"   -> EstadoReporte.RESUELTO
        else         -> EstadoReporte.PENDIENTE
    },
    fechaCreacion = fechaCreacion
)

fun ResumenReportesDto.toDomain(): ResumenReportes = ResumenReportes(
    total      = total,
    pendientes = pendientes,
    enProceso  = enProceso,
    resueltos  = resueltos
)
