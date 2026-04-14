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
import kotlinx.coroutines.flow.catch
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
            getMisReportesUseCase(idUsuario)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading    = false,
                            errorMessage = "No se pudieron cargar los reportes."
                        )
                    }
                }
                .collect { reportes ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading         = false,
                            todosLosReportes  = reportes,
                            reportesFiltrados = aplicarFiltros(
                                reportes,
                                state.filtroActivo,
                                state.busqueda
                            )
                        )
                    }
                }
        }
    }

    fun onFiltroChange(filtro: FiltroEstado) {
        _uiState.update { state ->
            state.copy(
                filtroActivo      = filtro,
                reportesFiltrados = aplicarFiltros(
                    state.todosLosReportes,
                    filtro,
                    state.busqueda
                )
            )
        }
    }

    fun onBusquedaChange(query: String) {
        _uiState.update { state ->
            state.copy(
                busqueda          = query,
                reportesFiltrados = aplicarFiltros(
                    state.todosLosReportes,
                    state.filtroActivo,
                    query
                )
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun aplicarFiltros(
        reportes: List<com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano>,
        filtro: FiltroEstado,
        busqueda: String
    ) = reportes
        .filter { reporte ->
            filtro.idEstado == null || reporte.idEstado == filtro.idEstado
        }
        .filter { reporte ->
            if (busqueda.isBlank()) true
            else reporte.titulo.contains(busqueda, ignoreCase = true) ||
                 reporte.descripcion.contains(busqueda, ignoreCase = true) ||
                 reporte.ubicacion.contains(busqueda, ignoreCase = true)
        }
}
