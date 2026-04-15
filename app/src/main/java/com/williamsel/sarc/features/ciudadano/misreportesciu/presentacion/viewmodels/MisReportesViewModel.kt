package com.williamsel.sarc.features.ciudadano.misreportesciu.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.usecases.GetMisReportesUseCase
import com.williamsel.sarc.features.ciudadano.misreportesciu.presentacion.screens.FiltroEstado
import com.williamsel.sarc.features.ciudadano.misreportesciu.presentacion.screens.MisReportesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MisReportesViewModel @Inject constructor(
    private val getMisReportesUseCase: GetMisReportesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MisReportesUiState())
    val uiState: StateFlow<MisReportesUiState> = _uiState.asStateFlow()

    fun cargarReportes(idUsuario: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val state = _uiState.value
            val result = getMisReportesUseCase(
                idUsuario = idUsuario,
                idEstado  = state.filtroActivo.idEstado,
                query     = state.busqueda
            )

            result.fold(
                onSuccess = { reportes ->
                    _uiState.update { 
                        it.copy(
                            isLoading         = false,
                            reportesFiltrados = reportes,
                            todosLosReportes  = reportes
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading    = false,
                            errorMessage = "No se pudieron cargar los reportes: ${e.message}"
                        )
                    }
                }
            )
        }
    }

    fun onFiltroChange(idUsuario: Int, filtro: FiltroEstado) {
        _uiState.update { it.copy(filtroActivo = filtro) }
        cargarReportes(idUsuario)
    }

    fun onBusquedaChange(idUsuario: Int, query: String) {
        _uiState.update { it.copy(busqueda = query) }
        cargarReportes(idUsuario)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}