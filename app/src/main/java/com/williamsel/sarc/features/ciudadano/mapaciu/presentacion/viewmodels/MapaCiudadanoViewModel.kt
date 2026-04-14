package com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroCategoriaDto
import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroEstadoDto
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.usecases.GetReportesMapaFiltradosUseCase
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.usecases.GetReportesMapaUseCase
import com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.screens.MapaCiudadanoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapaCiudadanoViewModel @Inject constructor(
    private val getReportesMapaUseCase:          GetReportesMapaUseCase,
    private val getReportesMapaFiltradosUseCase: GetReportesMapaFiltradosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaCiudadanoUiState())
    val uiState: StateFlow<MapaCiudadanoUiState> = _uiState.asStateFlow()

    init {
        cargarReportes()
    }

    fun cargarReportes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val reportes = getReportesMapaUseCase()
            _uiState.update { it.copy(isLoading = false, reportes = reportes) }
        }
    }

    fun onFiltroCategoriaChanged(filtro: FiltroCategoriaDto) {
        _uiState.update { it.copy(filtroCategoria = filtro) }
        aplicarFiltros()
    }

    fun onFiltroEstadoChanged(filtro: FiltroEstadoDto) {
        _uiState.update { it.copy(filtroEstado = filtro) }
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val reportes = getReportesMapaFiltradosUseCase(
                idIncidencia = state.filtroCategoria.id,
                idEstado     = state.filtroEstado.id
            )
            _uiState.update { it.copy(isLoading = false, reportes = reportes) }
        }
    }

    fun onMarcadorSeleccionado(reporte: ReporteMapa) {
        _uiState.update { it.copy(reporteSeleccionado = reporte) }
    }

    fun onCerrarDetalleReporte() {
        _uiState.update { it.copy(reporteSeleccionado = null) }
    }

    fun toggleFiltros() {
        _uiState.update { it.copy(mostrarFiltros = !it.mostrarFiltros) }
    }
}
