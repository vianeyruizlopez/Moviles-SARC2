package com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.usecases.GetEstadisticasGlobalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadisticasGlobalViewModel @Inject constructor(
    private val getEstadisticasGlobalUseCase: GetEstadisticasGlobalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstadisticasGlobalUiState())
    val uiState: StateFlow<EstadisticasGlobalUiState> = _uiState.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = getEstadisticasGlobalUseCase()
                _uiState.update { it.copy(isLoading = false, estadisticas = data) }
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

    fun clearError() { _uiState.update { it.copy(errorMessage = null) } }
}
