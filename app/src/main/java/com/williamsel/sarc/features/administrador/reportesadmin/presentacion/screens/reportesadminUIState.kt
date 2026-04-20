package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens

import androidx.compose.ui.graphics.Color

enum class EstadoFiltro(val label: String, val id: Int?) {
    TODOS("Todos", null),
    PENDIENTES("Pendientes", 1),
    EN_PROCESO("En Proceso", 2),
    RESUELTOS("Resueltos", 3)
}

data class ReporteAdminUiModel(
    val idReporte: Int,
    val titulo: String,
    val descripcion: String,
    val nombreEstado: String,
    val estadoColor: Color,
    val nombreIncidencia: String,
    val nombreUsuario: String,
    val ubicacion: String,
    val fechaFormateada: String,
    val imagen: String?,
    val idEstado: Int
)

data class ReportesAdminUiState(
    val isLoading: Boolean = false,
    val reportes: List<ReporteAdminUiModel> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val estadoSeleccionado: EstadoFiltro = EstadoFiltro.TODOS
)
