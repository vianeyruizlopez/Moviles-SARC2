package com.williamsel.sarc.features.administrador.mapaadmin.presentacion.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.williamsel.sarc.core.hardware.domain.LocationManager
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.usecases.GetReportesMapaUseCase
import com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens.MapaAdminUIState
import com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens.ReporteMapaUiModel
import com.williamsel.sarc.ui.theme.BlueProceso
import com.williamsel.sarc.ui.theme.GreenResuelto
import com.williamsel.sarc.ui.theme.OrangeWarning
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.TextMid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MapaAdminViewModel @Inject constructor(
    private val getReportesMapa: GetReportesMapaUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaAdminUIState())
    val uiState: StateFlow<MapaAdminUIState> = _uiState.asStateFlow()

    init {
        cargarReportes()
        verificarPermisoUbicacion()
    }

    fun cargarReportes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val idInc = _uiState.value.categoriaSeleccionada?.idIncidencia?.takeIf { it != 0 }
            val idEst = _uiState.value.estadoSeleccionado?.idEstado

            getReportesMapa(idInc, idEst)
                .onSuccess { lista ->
                    val uiModels = lista.map { it.toUiModel() }
                    _uiState.update { it.copy(isLoading = false, reportes = uiModels, error = null) }
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isLoading = false, error = err.message) }
                }
        }
    }

    private fun MapaReporte.toUiModel(): ReporteMapaUiModel {
        val fechaFormateada = try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(fecha)
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "MX")).format(date ?: Date())
        } catch (e: Exception) {
            fecha
        }

        return ReporteMapaUiModel(
            idReporte = idReporte,
            titulo = titulo,
            descripcion = descripcion,
            categoriaLabel = incidencia.etiqueta,
            categoriaColor = getCategoriaColor(incidencia.idIncidencia),
            estadoLabel = estado.etiqueta,
            estadoColor = when (estado.idEstado) {
                1 -> OrangeWarning
                2 -> BlueProceso
                3 -> GreenResuelto
                else -> TextMid
            },
            marcadorColor = when (estado.idEstado) {
                1 -> BitmapDescriptorFactory.HUE_ORANGE
                2 -> BitmapDescriptorFactory.HUE_AZURE
                3 -> BitmapDescriptorFactory.HUE_GREEN
                else -> BitmapDescriptorFactory.HUE_RED
            },
            latitud = latitud,
            longitud = longitud,
            coordenadasTexto = String.format(Locale.getDefault(), "%.6f, %.6f", latitud, longitud),
            fechaTexto = fechaFormateada
        )
    }

    private fun getCategoriaColor(id: Int): Color = when (id) {
        1 -> Color(0xFFE53935) // Rojo para Bache
        2 -> Color(0xFF43A047) // Verde para Basura
        3 -> Color(0xFFFB8C00) // Naranja para Alumbrado
        else -> SarcGreen
    }

    fun verificarPermisoUbicacion() {
        _uiState.update { 
            it.copy(tienePermisoUbicacion = locationManager.tienePermisoUbicacion()) 
        }
    }

    fun cambiarCategoria(categoria: CategoriaIncidencia?) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
        cargarReportes()
    }

    fun cambiarEstado(estado: EstadoMapaReporte?) {
        _uiState.update { it.copy(estadoSeleccionado = estado) }
        cargarReportes()
    }

    fun toggleFiltros() {
        _uiState.update { it.copy(mostrarFiltros = !it.mostrarFiltros) }
    }

    fun onMarcadorSeleccionado(reporte: ReporteMapaUiModel?) {
        _uiState.update { it.copy(reporteSeleccionado = reporte) }
    }

    fun onCerrarDetalleReporte() {
        _uiState.update { it.copy(reporteSeleccionado = null) }
    }
}
