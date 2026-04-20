package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.domain.usecases.GetReportesAdminUseCase
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens.EstadoFiltro
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens.ReporteAdminUiModel
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens.ReportesAdminUiState
import com.williamsel.sarc.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportesAdminViewModel @Inject constructor(
    private val getReportesAdminUseCase: GetReportesAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportesAdminUiState())
    val uiState: StateFlow<ReportesAdminUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        cargarReportes()
    }
    fun cargarReportes(
        filtro: EstadoFiltro = _uiState.value.estadoSeleccionado, 
        query: String = _uiState.value.searchQuery
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, estadoSeleccionado = filtro, searchQuery = query) }
            
            val result = getReportesAdminUseCase(filtro.id, if (query.isEmpty()) null else query)

            result.onSuccess { reportes ->
                val uiModels = reportes.map { it.toUiModel() }
                _uiState.update { it.copy(isLoading = false, reportes = uiModels) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = "No se pudieron cargar los reportes") }
            }
        }
    }

    private fun ReporteAdmin.toUiModel() = ReporteAdminUiModel(
        idReporte = this.idReporte,
        titulo = this.titulo,
        descripcion = this.descripcion,
        nombreEstado = this.nombreEstado,
        idEstado = this.idEstado,
        estadoColor = when (this.idEstado) {
            1 -> OrangeWarning
            2 -> BlueProceso
            3 -> GreenResuelto
            else -> TextMid
        },
        nombreIncidencia = this.nombreIncidencia,
        nombreUsuario = this.nombreUsuario,
        ubicacion = this.ubicacion ?: "Sin ubicación",
        fechaFormateada = formatFecha(this.fecha),
        imagen = this.imagen
    )

    private fun formatFecha(fecha: String): String = try {
        val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val dt = LocalDateTime.parse(fecha, inputFormatter)
        dt.format(DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", Locale("es", "MX")))
    } catch (e: Exception) { 
        fecha
    }

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            cargarReportes(query = newQuery)
        }
    }

    fun onEstadoFilterChanged(filtro: EstadoFiltro) {
        cargarReportes(filtro = filtro)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
