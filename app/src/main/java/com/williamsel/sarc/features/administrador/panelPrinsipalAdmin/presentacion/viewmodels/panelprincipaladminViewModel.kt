package com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.usecases.GetResumenReportesUseCase
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.presentacion.screens.PanelPrincipalAdminUIState
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.presentacion.screens.PanelPrincipalUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelPrincipalAdminViewModel @Inject constructor(
    private val getResumenReportes: GetResumenReportesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PanelPrincipalAdminUIState>(PanelPrincipalAdminUIState.Loading)
    val uiState: StateFlow<PanelPrincipalAdminUIState> = _uiState.asStateFlow()

    init {
        cargarResumen()
    }

    fun cargarResumen() {
        viewModelScope.launch {
            _uiState.value = PanelPrincipalAdminUIState.Loading
            getResumenReportes()
                .onSuccess { resumen ->
                    _uiState.value = PanelPrincipalAdminUIState.Success(mapToUIModel(resumen))
                }
                .onFailure { error ->
                    _uiState.value = PanelPrincipalAdminUIState.Error(
                        error.message ?: "Error al cargar resumen"
                    )
                }
        }
    }

    private fun mapToUIModel(domain: ResumenReportes): PanelPrincipalUIModel {
        return PanelPrincipalUIModel(
            totalReportes = domain.total.toString(),
            pendientes    = domain.pendientes.toString(),
            enProceso     = domain.enProceso.toString(),
            resueltos     = domain.resueltos.toString()
        )
    }
}
