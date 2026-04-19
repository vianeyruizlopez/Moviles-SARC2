package com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.screens

import androidx.compose.ui.graphics.Color

data class DetalleReporteUIModel(
    val idReporte: Int,
    val titulo: String,
    val descripcion: String,
    val nombreUsuario: String,
    val nombreIncidencia: String,
    val categoriaEmoji: String,
    val nombreEstado: String,
    val idEstado: Int,
    val estadoColor: Color,
    val ubicacion: String?,
    val latitud: Double?,
    val longitud: Double?,
    val imagen: String?,
    val fechaFormateada: String
)

sealed class DetalleReporteAdminUIState {
    data object Loading : DetalleReporteAdminUIState()
    data class Success(val reporte: DetalleReporteUIModel) : DetalleReporteAdminUIState()
    data class Error(val message: String) : DetalleReporteAdminUIState()
}
