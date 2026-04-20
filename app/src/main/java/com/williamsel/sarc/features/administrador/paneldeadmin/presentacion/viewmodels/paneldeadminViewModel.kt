package com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.williamsel.sarc.core.hardware.domain.LocationManager
import com.williamsel.sarc.features.administrador.panelPrinsipalAdmin.domain.entities.ResumenReportes
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.usecases.GetPanelDeAdminUseCase
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens.PanelDeAdminUIState
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens.PanelEstadisticasUIModel
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens.PanelReporteUIModel
import com.williamsel.sarc.ui.theme.BlueProceso
import com.williamsel.sarc.ui.theme.GreenResuelto
import com.williamsel.sarc.ui.theme.OrangeWarning
import com.williamsel.sarc.ui.theme.TextMid
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
    private val useCase: GetPanelDeAdminUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<PanelDeAdminUIState>(PanelDeAdminUIState.Loading)
    val uiState: StateFlow<PanelDeAdminUIState> = _uiState.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    private val _estadoSeleccionado = MutableStateFlow("Todos")
    val estadoSeleccionado: StateFlow<String> = _estadoSeleccionado.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _vistaActiva = MutableStateFlow(VistaPanel.LISTA)
    val vistaActiva: StateFlow<VistaPanel> = _vistaActiva.asStateFlow()

    private var lastReportes: List<PanelReporte> = emptyList()
    private var lastResumen: ResumenReportes = ResumenReportes(0, 0, 0, 0)
    private var miUbicacionActual: LatLng? = null

    val categorias = listOf("Todos", "Bache", "Basura", "Alumbrado")
    private var searchJob: Job? = null

    init {
        cargarTodo()
    }

    private fun cargarTodo() {
        viewModelScope.launch {
            _uiState.value = PanelDeAdminUIState.Loading
            
            // Intentar obtener ubicación
            if (locationManager.tienePermisoUbicacion()) {
                locationManager.obtenerUbicacion()?.let {
                    miUbicacionActual = LatLng(it.latitud, it.longitud)
                }
            }

            val resumenResult = useCase.getResumenReportes()
            val reportesResult = useCase.getReportes(null, null)

            if (resumenResult.isSuccess && reportesResult.isSuccess) {
                lastResumen = resumenResult.getOrThrow()
                lastReportes = reportesResult.getOrThrow()
                actualizarEstadoExitoso()
            } else {
                _uiState.value = PanelDeAdminUIState.Error("Error al cargar datos iniciales")
            }
        }
    }

    fun cargarReportes() {
        viewModelScope.launch {
            val idEstado = when(_estadoSeleccionado.value) {
                "Pendiente" -> 1
                "En Proceso" -> 2
                "Resuelto" -> 3
                else -> null
            }
            val idIncidencia = when(_categoriaSeleccionada.value) {
                "Bache" -> 1
                "Basura" -> 2
                "Alumbrado" -> 3
                else -> null
            }

            useCase.getReportes(idIncidencia, idEstado).onSuccess {
                lastReportes = it
                actualizarEstadoExitoso()
            }
        }
    }

    private fun actualizarEstadoExitoso() {
        _uiState.value = PanelDeAdminUIState.Success(
            reportes = lastReportes.map { it.toUIModel() },
            estadisticas = lastResumen.toUIModel(),
            miUbicacion = miUbicacionActual
        )
    }

    private fun PanelReporte.toUIModel() = PanelReporteUIModel(
        idReporte = idReporte,
        titulo = titulo,
        subtitulo = "$nombreIncidencia • $nombreUsuario",
        colorEstado = when(idEstado) {
            1 -> OrangeWarning
            2 -> BlueProceso
            3 -> GreenResuelto
            else -> TextMid
        },
        markerHue = when(idEstado) {
            1 -> BitmapDescriptorFactory.HUE_ORANGE
            2 -> BitmapDescriptorFactory.HUE_AZURE
            3 -> BitmapDescriptorFactory.HUE_GREEN
            else -> BitmapDescriptorFactory.HUE_RED
        },
        idEstado = idEstado,
        latitud = latitud,
        longitud = longitud
    )

    private fun ResumenReportes.toUIModel() = PanelEstadisticasUIModel(
        total = total.toString(),
        pendientes = pendientes.toString(),
        enProceso = enProceso.toString(),
        resueltos = resueltos.toString()
    )

    fun actualizarBusqueda(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (query.length > 2) {
                _uiState.value = PanelDeAdminUIState.Loading
                useCase.buscarReportes(query).onSuccess {
                    lastReportes = it
                    actualizarEstadoExitoso()
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
                useCase.getResumenReportes().onSuccess { lastResumen = it }
                cargarReportes()
            }
        }
    }

    fun eliminarReporte(idReporte: Int) {
        viewModelScope.launch {
            useCase.eliminarReporte(idReporte).onSuccess {
                useCase.getResumenReportes().onSuccess { lastResumen = it }
                cargarReportes()
            }
        }
    }
}

enum class VistaPanel { LISTA, MAPA }
