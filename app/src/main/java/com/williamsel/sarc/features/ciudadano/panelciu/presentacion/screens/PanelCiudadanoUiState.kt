package com.williamsel.sarc.features.ciudadano.panelciu.presentacion.screens

data class PanelCiudadanoUiState(
    val isLoading:      Boolean = false,
    val nombreCompleto: String  = "",
    val total:          Int     = 0,
    val pendientes:     Int     = 0,
    val enProceso:      Int     = 0,
    val resueltos:      Int     = 0,
    val errorMessage:   String? = null
)
