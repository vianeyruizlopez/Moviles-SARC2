package com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.screens

import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin

sealed class DetalleReporteAdminUIState {
    data object Loading : DetalleReporteAdminUIState()
    data class Success(val reporte: DetalleReporteAdmin) : DetalleReporteAdminUIState()
    data class Error(val message: String) : DetalleReporteAdminUIState()
}
