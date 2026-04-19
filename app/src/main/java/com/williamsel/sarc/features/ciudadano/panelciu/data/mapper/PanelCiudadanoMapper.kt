package com.williamsel.sarc.features.ciudadano.panelciu.data.mapper

import com.williamsel.sarc.features.ciudadano.panelciu.data.models.PanelCiudadanoDto
import com.williamsel.sarc.features.ciudadano.panelciu.domain.entities.PanelCiudadano

fun PanelCiudadanoDto.toDomain(): PanelCiudadano = PanelCiudadano(
    total      = total,
    pendientes = pendientes,
    enProceso  = enProceso,
    resueltos  = resueltos
)
