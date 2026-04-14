package com.williamsel.sarc.features.administrador.paneladmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.paneladmin.domain.usecases.GetResumenReportesUseCase
import com.williamsel.sarc.features.administrador.paneladmin.presentacion.screens.PanelPrincipalAdminUIState
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

    private val _uiState = MutableStateFlow<PanelPrincipalAdminUIState>(
        PanelPrincipalAdminUIState.Loading
    )
    val uiState: StateFlow<PanelPrincipalAdminUIState> = _uiState.asStateFlow()

    init {
        cargarResumen()
    }

    fun cargarResumen() {
        viewModelScope.launch {
            _uiState.value = PanelPrincipalAdminUIState.Loading
            getResumenReportes()
                .onSuccess { resumen ->
                    _uiState.value = PanelPrincipalAdminUIState.Success(resumen)
                }
                .onFailure { error ->
                    _uiState.value = PanelPrincipalAdminUIState.Error(
                        error.message ?: "Error desconocido"
                    )
                }
        }
    }
}
