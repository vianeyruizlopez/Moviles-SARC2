package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.mapper

import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.data.models.ResumenReportesDto
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes

fun ResumenReportesDto.toDomain(): ResumenReportes = ResumenReportes(
    total      = total,
    pendientes = pendientes,
    enProceso  = enProceso,
    resueltos  = resueltos
)
