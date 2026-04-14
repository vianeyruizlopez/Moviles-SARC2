package com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.viewmodels

import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.EstadisticasGlobal

data class EstadisticasGlobalUiState(
    val isLoading: Boolean          = false,
    val estadisticas: EstadisticasGlobal? = null,
    val errorMessage: String?       = null
)
