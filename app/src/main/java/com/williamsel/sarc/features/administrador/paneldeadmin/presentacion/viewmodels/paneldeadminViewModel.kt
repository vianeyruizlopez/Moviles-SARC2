package com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.paneladmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.usecases.GetPanelDeAdminUseCase
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens.PanelDeAdminUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelDeAdminViewModel @Inject constructor(
    private val useCase: GetPanelDeAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PanelDeAdminUIState>(PanelDeAdminUIState.Loading)
    val uiState: StateFlow<PanelDeAdminUIState> = _uiState.asStateFlow()

    private val _resumen = MutableStateFlow(ResumenReportes(0, 0, 0, 0))
    val resumen: StateFlow<ResumenReportes> = _resumen.asStateFlow()

    private val _filteredReportes = MutableStateFlow<List<PanelReporte>>(emptyList())
    val filteredReportes: StateFlow<List<PanelReporte>> = _filteredReportes.asStateFlow()

    // Filtros
    private val _categoriaSeleccionada = MutableStateFlow("Todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    private val _estadoSeleccionado = MutableStateFlow("Todos")
    val estadoSeleccionado: StateFlow<String> = _estadoSeleccionado.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _vistaActiva = MutableStateFlow(VistaPanel.LISTA)
    val vistaActiva: StateFlow<VistaPanel> = _vistaActiva.asStateFlow()

    // Lista de categorías sincronizadas con la base de datos
    val categorias = listOf("Todos", "Bache", "Basura", "Alumbrado")

    private var searchJob: Job? = null

    init {
        cargarEstadisticas()
        cargarReportes()
    }

    fun cargarEstadisticas() {
        viewModelScope.launch {
            useCase.getResumenReportes().onSuccess {
                _resumen.value = it
            }
        }
    }

    fun cargarReportes() {
        viewModelScope.launch {
            _uiState.value = PanelDeAdminUIState.Loading
            
            val idEstado = when(_estadoSeleccionado.value) {
                "Pendiente" -> 1
                "En Proceso" -> 2
                "Resuelto" -> 3
                else -> null
            }
            
            // Mapeo según id_incidencia de la base de datos
            val idIncidencia = when(_categoriaSeleccionada.value) {
                "Bache" -> 1
                "Basura" -> 2
                "Alumbrado" -> 3
                else -> null
            }

            useCase.getReportes(idIncidencia, idEstado).onSuccess {
                _filteredReportes.value = it
                _uiState.value = PanelDeAdminUIState.Success(it)
            }.onFailure {
                _uiState.value = PanelDeAdminUIState.Error(it.message ?: "Error desconocido")
            }
        }
    }

    fun actualizarBusqueda(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            if (query.length > 2) {
                _uiState.value = PanelDeAdminUIState.Loading
                useCase.buscarReportes(query).onSuccess {
                    _filteredReportes.value = it
                    _uiState.value = PanelDeAdminUIState.Success(it)
                }
            } else if (query.isEmpty()) {
                cargarReportes()
            }
        }
    }

    fun cambiarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
        cargarReportes()
    }

    fun cambiarEstado(estado: String) {
        _estadoSeleccionado.value = estado
        cargarReportes()
    }

    fun cambiarVista(vista: VistaPanel) {
        _vistaActiva.value = vista
    }

    fun actualizarEstadoReporte(idReporte: Int, nuevoEstado: Int) {
        viewModelScope.launch {
            useCase.actualizarEstado(idReporte, nuevoEstado).onSuccess {
                cargarEstadisticas() // Refrescar conteo
                cargarReportes()     // Refrescar lista
            }
        }
    }

    fun eliminarReporte(idReporte: Int) {
        viewModelScope.launch {
            useCase.eliminarReporte(idReporte).onSuccess {
                cargarEstadisticas()
                cargarReportes()
            }
        }
    }
}

enum class VistaPanel { LISTA, MAPA }
