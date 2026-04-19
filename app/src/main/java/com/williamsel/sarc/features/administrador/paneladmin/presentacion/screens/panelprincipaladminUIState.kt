package com.williamsel.sarc.features.administrador.paneladmin.presentacion.screens

data class PanelPrincipalUIModel(
    val totalReportes: String,
    val pendientes: String,
    val enProceso: String,
    val resueltos: String
)

sealed class PanelPrincipalAdminUIState {
    data object Loading : PanelPrincipalAdminUIState()
    data class Success(val uiModel: PanelPrincipalUIModel) : PanelPrincipalAdminUIState()
    data class Error(val mensaje: String) : PanelPrincipalAdminUIState()
}
