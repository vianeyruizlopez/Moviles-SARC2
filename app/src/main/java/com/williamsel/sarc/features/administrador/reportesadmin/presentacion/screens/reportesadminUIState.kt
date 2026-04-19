package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens

import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.EstadoFiltro
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.ReporteAdminUiModel

data class ReportesAdminUiState(
    val isLoading: Boolean = false,
    val reportes: List<ReporteAdminUiModel> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val estadoSeleccionado: EstadoFiltro = EstadoFiltro.TODOS
)
