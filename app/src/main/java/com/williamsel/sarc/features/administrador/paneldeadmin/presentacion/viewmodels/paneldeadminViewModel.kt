package com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.usecases.GetPanelDeAdminUseCase
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens.PanelDeAdminUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelDeAdminViewModel @Inject constructor(
    private val useCase: GetPanelDeAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PanelDeAdminUIState>(PanelDeAdminUIState.Loading)
    val uiState: StateFlow<PanelDeAdminUIState> = _uiState.asStateFlow()

    // Todos los reportes sin filtrar (para estadísticas)
    private val _allReportes = MutableStateFlow<List<PanelReporte>>(emptyList())

    // Filtros
    private val _categoriaSeleccionada = MutableStateFlow<String>("Todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    private val _estadoSeleccionado = MutableStateFlow<String>("Todos")
    val estadoSeleccionado: StateFlow<String> = _estadoSeleccionado.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Reportes filtrados
    private val _filteredReportes = MutableStateFlow<List<PanelReporte>>(emptyList())
    val filteredReportes: StateFlow<List<PanelReporte>> = _filteredReportes.asStateFlow()

    // Vista activa (lista o mapa)
    private val _vistaActiva = MutableStateFlow(VistaPanel.LISTA)
    val vistaActiva: StateFlow<VistaPanel> = _vistaActiva.asStateFlow()

    // Estadísticas
    val totalReportes: Int get() = _allReportes.value.size
    val pendientes: Int get() = _allReportes.value.count { it.idEstado == 1 }
    val enProceso: Int get() = _allReportes.value.count { it.idEstado == 2 }
    val resueltos: Int get() = _allReportes.value.count { it.idEstado == 3 }

    // Lista de categorías disponibles
    val categorias: List<String>
        get() = listOf("Todos") + _allReportes.value
            .map { it.nombreIncidencia }
            .distinct()
            .sorted()

    init {
        viewModelScope.launch {
            combine(
                _allReportes,
                _categoriaSeleccionada,
                _estadoSeleccionado,
                _searchQuery
            ) { reportes, categoria, estado, query ->
                var filtered = reportes

                if (categoria != "Todos") {
                    filtered = filtered.filter { it.nombreIncidencia == categoria }
                }

                if (estado != "Todos") {
                    filtered = filtered.filter { it.nombreEstado == estado }
                }

                if (query.isNotBlank()) {
                    filtered = filtered.filter { reporte ->
                        reporte.titulo.contains(query, ignoreCase = true) ||
                        reporte.descripcion.contains(query, ignoreCase = true) ||
                        reporte.ubicacion.orEmpty().contains(query, ignoreCase = true)
                    }
                }

                filtered
            }.collect { _filteredReportes.value = it }
        }
        cargarReportes()
    }

    fun cargarReportes() {
        viewModelScope.launch {
            _uiState.value = PanelDeAdminUIState.Loading
            try {
                val reportes = useCase.getReportes()
                _allReportes.value = reportes
                _uiState.value = PanelDeAdminUIState.Success(reportes)
            } catch (e: Exception) {
                _uiState.value = PanelDeAdminUIState.Error(
                    e.localizedMessage ?: "Error al cargar reportes"
                )
            }
        }
    }

    fun cambiarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    fun cambiarEstado(estado: String) {
        _estadoSeleccionado.value = estado
    }

    fun actualizarBusqueda(query: String) {
        _searchQuery.value = query
    }

    fun cambiarVista(vista: VistaPanel) {
        _vistaActiva.value = vista
    }

    fun actualizarEstadoReporte(idReporte: Int, nuevoEstado: Int) {
        viewModelScope.launch {
            try {
                useCase.actualizarEstado(idReporte, nuevoEstado)
                cargarReportes() // recargar toda la lista
            } catch (e: Exception) {
                // El error se maneja en la UI si es necesario
            }
        }
    }

    fun eliminarReporte(idReporte: Int) {
        viewModelScope.launch {
            try {
                useCase.eliminarReporte(idReporte)
                cargarReportes()
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}

enum class VistaPanel { LISTA, MAPA }
