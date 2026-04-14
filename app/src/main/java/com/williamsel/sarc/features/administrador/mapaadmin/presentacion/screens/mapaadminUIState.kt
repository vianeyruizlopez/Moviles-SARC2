package com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens

import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte

sealed class MapaAdminUIState {
    object Loading : MapaAdminUIState()
    data class Success(val reportes: List<MapaReporte>) : MapaAdminUIState()
    data class Error(val mensaje: String) : MapaAdminUIState()
}
