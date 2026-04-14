package com.williamsel.sarc.features.ciudadano.misreportesciu.presentacion.screens

import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano

enum class FiltroEstado(val label: String, val idEstado: Int?) {
    TODOS("Todos", null),
    PENDIENTE("Pendientes", 1),
    EN_PROCESO("En Proceso", 2),
    RESUELTO("Resueltos", 3)
}

data class MisReportesUiState(
    val isLoading: Boolean                  = false,
    val todosLosReportes: List<ReporteCiudadano> = emptyList(),
    val reportesFiltrados: List<ReporteCiudadano> = emptyList(),
    val filtroActivo: FiltroEstado          = FiltroEstado.TODOS,
    val busqueda: String                    = "",
    val errorMessage: String?               = null
)
