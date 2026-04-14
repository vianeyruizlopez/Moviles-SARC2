package com.williamsel.sarc.features.ciudadano.panelciu.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.ciudadano.panelciu.domain.usecases.GetPanelCiudadanoUseCase
import com.williamsel.sarc.features.ciudadano.panelciu.presentacion.screens.PanelCiudadanoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelCiudadanoViewModel @Inject constructor(
    private val getPanelCiudadanoUseCase: GetPanelCiudadanoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PanelCiudadanoUiState())
    val uiState: StateFlow<PanelCiudadanoUiState> = _uiState.asStateFlow()

    fun cargarPanel(idUsuario: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = getPanelCiudadanoUseCase(idUsuario)
                _uiState.update {
                    it.copy(
                        isLoading      = false,
                        nombreCompleto = data.nombreCompleto,
                        totalReportes  = data.totalReportes,
                        pendientes     = data.pendientes,
                        enProceso      = data.enProceso,
                        resueltos      = data.resueltos
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading    = false,
                        errorMessage = "No se pudo cargar el panel. Intenta de nuevo."
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
