package com.williamsel.sarc.features.superadmin.panelsuperadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.superadmin.panelsuperadmin.domain.usecases.GetPanelSuperAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelSuperAdminViewModel @Inject constructor(
    private val getPanelSuperAdminUseCase: GetPanelSuperAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PanelSuperAdminUiState())
    val uiState: StateFlow<PanelSuperAdminUiState> = _uiState.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = getPanelSuperAdminUseCase()
                _uiState.update {
                    it.copy(
                        isLoading       = false,
                        nombreCompleto  = data.nombreCompleto,
                        totalReportes   = data.totalReportes,
                        pendientes      = data.pendientes,
                        enProceso       = data.enProceso,
                        resueltos       = data.resueltos,
                        totalAdmins     = data.totalAdmins,
                        totalCiudadanos = data.totalCiudadanos
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "No se pudo cargar el panel.")
                }
            }
        }
    }

    fun clearError() { _uiState.update { it.copy(errorMessage = null) } }
}
