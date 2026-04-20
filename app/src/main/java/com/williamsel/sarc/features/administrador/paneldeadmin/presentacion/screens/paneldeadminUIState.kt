package com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens

import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng

data class PanelReporteUIModel(
    val idReporte: Int,
    val titulo: String,
    val subtitulo: String, 
    val colorEstado: Color,
    val markerHue: Float, 
    val idEstado: Int,
    val latitud: Double?,
    val longitud: Double?
)

data class PanelEstadisticasUIModel(
    val total: String,
    val pendientes: String,
    val enProceso: String,
    val resueltos: String
)

sealed class PanelDeAdminUIState {
    data object Loading : PanelDeAdminUIState()
    data class Success(
        val reportes: List<PanelReporteUIModel>,
        val estadisticas: PanelEstadisticasUIModel,
        val miUbicacion: LatLng? = null
    ) : PanelDeAdminUIState()
    data class Error(val mensaje: String) : PanelDeAdminUIState()
}
