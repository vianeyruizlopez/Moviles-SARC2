package com.williamsel.sarc.features.administrador.paneladmin.data.mapper

import com.williamsel.sarc.features.administrador.paneladmin.data.models.ResumenReportesDto
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes

fun ResumenReportesDto.toDomain(): ResumenReportes = ResumenReportes(
    total      = total,
    pendientes = pendientes,
    enProceso  = enProceso,
    resueltos  = resueltos
)
