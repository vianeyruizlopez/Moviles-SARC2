package com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens

import androidx.compose.ui.graphics.Color
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte

data class ReporteMapaUiModel(
    val idReporte: Int,
    val titulo: String,
    val descripcion: String,
    val categoriaLabel: String,
    val categoriaColor: Color,
    val estadoLabel: String,
    val estadoColor: Color,
    val marcadorColor: Float,
    val latitud: Double,
    val longitud: Double,
    val coordenadasTexto: String,
    val fechaTexto: String?
)

data class MapaAdminUIState(
    val reportes: List<ReporteMapaUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val categoriaSeleccionada: CategoriaIncidencia? = null,
    val estadoSeleccionado: EstadoMapaReporte? = null,
    val reporteSeleccionado: ReporteMapaUiModel? = null,
    val mostrarFiltros: Boolean = true,
    val tienePermisoUbicacion: Boolean = false
)
