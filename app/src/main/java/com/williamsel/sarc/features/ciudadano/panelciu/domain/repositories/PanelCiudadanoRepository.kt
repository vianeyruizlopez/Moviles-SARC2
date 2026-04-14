package com.williamsel.sarc.features.ciudadano.panelciu.domain.repositories

import com.williamsel.sarc.features.ciudadano.panelciu.domain.entities.PanelCiudadano

interface PanelCiudadanoRepository {
    suspend fun getPanelData(idUsuario: Int): PanelCiudadano
}
