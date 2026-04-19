package com.williamsel.sarc.features.ciudadano.panelciu.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.core.session.SessionManager
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
    private val getPanelCiudadanoUseCase: GetPanelCiudadanoUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PanelCiudadanoUiState())
    val uiState: StateFlow<PanelCiudadanoUiState> = _uiState.asStateFlow()

    init {
        cargarPanel()
    }

    fun cargarPanel() {
        val idUsuario = sessionManager.getUserId()
        if (idUsuario == -1) {
            _uiState.update { it.copy(errorMessage = "Sesión no válida") }
            return
        }

        val etiquetaUsuario = "Ciudadano SARC"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, nombreCompleto = etiquetaUsuario) }
            try {
                val data = getPanelCiudadanoUseCase(idUsuario)
                _uiState.update {
                    it.copy(
                        isLoading      = false,
                        total          = data.total,
                        pendientes     = data.pendientes,
                        enProceso      = data.enProceso,
                        resueltos      = data.resueltos
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading    = false,
                        errorMessage = "No se pudieron cargar las estadísticas."
                    )
                }
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
