package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens

import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin

sealed class ReportesAdminUIState {
    data object Loading : ReportesAdminUIState()
    data class Success(val reportes: List<ReporteAdmin>) : ReportesAdminUIState()
    data class Error(val message: String) : ReportesAdminUIState()
}
