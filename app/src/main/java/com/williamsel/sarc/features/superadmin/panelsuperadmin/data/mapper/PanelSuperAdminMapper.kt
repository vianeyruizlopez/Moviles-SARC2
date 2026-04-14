package com.williamsel.sarc.features.superadmin.panelsuperadmin.data.mapper

import com.williamsel.sarc.features.superadmin.panelsuperadmin.data.models.PanelSuperAdminDto
import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.entities.PanelSuperAdmin

fun PanelSuperAdminDto.toDomain() = PanelSuperAdmin(
    nombreCompleto  = "$nombre $primerApellido $segundoApellido".trim(),
    totalReportes   = totalReportes,
    pendientes      = pendientes,
    enProceso       = enProceso,
    resueltos       = resueltos,
    totalAdmins     = totalAdmins,
    totalCiudadanos = totalCiudadanos
)
