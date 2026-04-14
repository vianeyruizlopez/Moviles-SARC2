package com.williamsel.sarc.features.administrador.mapaadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.usecases.GetReportesMapaUseCase
import com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens.MapaAdminUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapaAdminViewModel @Inject constructor(
    private val getReportesMapa: GetReportesMapaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapaAdminUIState>(MapaAdminUIState.Loading)
    val uiState: StateFlow<MapaAdminUIState> = _uiState.asStateFlow()

    // null = "Todos"
    private val _categoriaSeleccionada = MutableStateFlow<CategoriaIncidencia?>(null)
    val categoriaSeleccionada: StateFlow<CategoriaIncidencia?> = _categoriaSeleccionada.asStateFlow()

    private val _estadoSeleccionado = MutableStateFlow<EstadoMapaReporte?>(null)
    val estadoSeleccionado: StateFlow<EstadoMapaReporte?> = _estadoSeleccionado.asStateFlow()

    init { cargarReportes() }

    fun cargarReportes() {
        viewModelScope.launch {
            _uiState.value = MapaAdminUIState.Loading
            val idInc = _categoriaSeleccionada.value?.idIncidencia
                ?.takeIf { it != 0 }  // 0 = TODOS, no filtra
            val idEst = _estadoSeleccionado.value?.idEstado
            getReportesMapa(idInc, idEst)
                .onSuccess { lista -> _uiState.value = MapaAdminUIState.Success(lista) }
                .onFailure { err  -> _uiState.value = MapaAdminUIState.Error(err.message ?: "Error") }
        }
    }

    fun cambiarCategoria(categoria: CategoriaIncidencia?) {
        _categoriaSeleccionada.value = categoria
        cargarReportes()
    }

    fun cambiarEstado(estado: EstadoMapaReporte?) {
        _estadoSeleccionado.value = estado
        cargarReportes()
    }
}
