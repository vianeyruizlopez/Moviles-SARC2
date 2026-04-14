package com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens

import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte

sealed class PanelDeAdminUIState {
    data object Loading : PanelDeAdminUIState()
    data class Success(val reportes: List<PanelReporte>) : PanelDeAdminUIState()
    data class Error(val message: String) : PanelDeAdminUIState()
}
