package com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.williamsel.sarc.core.hardware.domain.LocationManager
import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroCategoriaDto
import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroEstadoDto
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.usecases.GetReportesMapaUseCase
import com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.screens.MapaCiudadanoUiState
import com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.screens.ReporteMapaUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapaCiudadanoViewModel @Inject constructor(
    private val getReportesMapaUseCase: GetReportesMapaUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaCiudadanoUiState())
    val uiState: StateFlow<MapaCiudadanoUiState> = _uiState.asStateFlow()

    init {
        cargarReportes()
        verificarPermisoUbicacion()
    }

    fun cargarReportes() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val reportesDomain = getReportesMapaUseCase(
                    idIncidencia = state.filtroCategoria.id,
                    idEstado     = state.filtroEstado.id
                )
                val reportesUi = reportesDomain.map { it.toUiModel() }
                _uiState.update { it.copy(isLoading = false, reportes = reportesUi) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading    = false, 
                        errorMessage = "Error al conectar con el servidor" 
                    )
                }
            }
        }
    }

    fun verificarPermisoUbicacion() {
        _uiState.update { 
            it.copy(tienePermisoUbicacion = locationManager.tienePermisoUbicacion()) 
        }
    }

    fun onFiltroCategoriaChanged(filtro: FiltroCategoriaDto) {
        _uiState.update { it.copy(filtroCategoria = filtro) }
        cargarReportes()
    }

    fun onFiltroEstadoChanged(filtro: FiltroEstadoDto) {
        _uiState.update { it.copy(filtroEstado = filtro) }
        cargarReportes()
    }

    fun onMarcadorSeleccionado(reporte: ReporteMapaUiModel) {
        _uiState.update { it.copy(reporteSeleccionado = reporte) }
    }

    fun onCerrarDetalleReporte() {
        _uiState.update { it.copy(reporteSeleccionado = null) }
    }

    fun toggleFiltros() {
        _uiState.update { it.copy(mostrarFiltros = !it.mostrarFiltros) }
    }

    // --- Mapeo de Dominio a UI (Lógica de Presentación) ---

    private fun ReporteMapa.toUiModel(): ReporteMapaUiModel {
        return ReporteMapaUiModel(
            idReporte        = idReporte,
            nombre           = nombre,
            descripcion      = descripcion,
            latitud          = latitud,
            longitud         = longitud,
            coordenadasTexto = "%.6f, %.6f".format(latitud, longitud),
            fechaTexto       = fechaReporte?.let { 
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "MX")).format(Date(it)) 
            },
            categoriaLabel   = getCategoriaLabel(idIncidencia),
            categoriaColor   = getCategoriaColor(idIncidencia),
            marcadorColor    = getMarcadorColor(idIncidencia),
            estadoLabel      = getEstadoLabel(idEstado),
            estadoColor      = getEstadoColor(idEstado)
        )
    }

    private fun getCategoriaLabel(id: Int?): String = when (id) {
        1 -> "Bache"
        2 -> "Basura"
        3 -> "Alumbrado"
        else -> "Incidencia"
    }

    private fun getCategoriaColor(id: Int?): Color = when (id) {
        1 -> Color(0xFFE53935)
        2 -> Color(0xFFFBC02D)
        3 -> Color(0xFFF57C00)
        else -> Color(0xFF757575)
    }

    private fun getMarcadorColor(id: Int?): Float = when (id) {
        1 -> BitmapDescriptorFactory.HUE_RED
        2 -> BitmapDescriptorFactory.HUE_YELLOW
        3 -> BitmapDescriptorFactory.HUE_ORANGE
        else -> BitmapDescriptorFactory.HUE_RED
    }

    private fun getEstadoLabel(id: Int?): String = when (id) {
        1 -> "Pendiente"
        2 -> "En Proceso"
        3 -> "Resuelto"
        else -> "Desconocido"
    }

    private fun getEstadoColor(id: Int?): Color = when (id) {
        1 -> Color(0xFFFFECB3)
        2 -> Color(0xFFBBDEFB)
        3 -> Color(0xFFC8E6C9)
        else -> Color(0xFFF5F5F5)
    }
}
