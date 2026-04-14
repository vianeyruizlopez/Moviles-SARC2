package com.williamsel.sarc.features.administrador.paneladmin.presentacion.screens

import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes

sealed class PanelPrincipalAdminUIState {
    object Loading : PanelPrincipalAdminUIState()
    data class Success(val resumen: ResumenReportes) : PanelPrincipalAdminUIState()
    data class Error(val mensaje: String) : PanelPrincipalAdminUIState()
}
