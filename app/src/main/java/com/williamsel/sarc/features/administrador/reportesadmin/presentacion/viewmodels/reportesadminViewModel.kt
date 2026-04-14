package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.domain.usecases.GetReportesAdminUseCase
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens.ReportesAdminUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class EstadoFiltro(val idEstado: Int?, val label: String) {
    TODOS(null, "Todos"),
    PENDIENTES(1, "Pendientes"),
    EN_PROCESO(2, "En Proceso"),
    RESUELTOS(3, "Resueltos")
}

@HiltViewModel
class ReportesAdminViewModel @Inject constructor(
    private val getReportesAdminUseCase: GetReportesAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReportesAdminUIState>(ReportesAdminUIState.Loading)
    val uiState: StateFlow<ReportesAdminUIState> = _uiState.asStateFlow()

    private val _filtroActual = MutableStateFlow(EstadoFiltro.TODOS)
    val filtroActual: StateFlow<EstadoFiltro> = _filtroActual.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allReportes = MutableStateFlow<List<ReporteAdmin>>(emptyList())

    private val _filteredReportes = MutableStateFlow<List<ReporteAdmin>>(emptyList())
    val filteredReportes: StateFlow<List<ReporteAdmin>> = _filteredReportes.asStateFlow()

    private var currentUserId: Int = -1

    init {
        viewModelScope.launch {
            combine(_allReportes, _filtroActual, _searchQuery) { reportes, filtro, query ->
                val filteredByState = if (filtro.idEstado == null) {
                    reportes
                } else {
                    reportes.filter { it.idEstado == filtro.idEstado }
                }

                if (query.isBlank()) {
                    filteredByState
                } else {
                    filteredByState.filter { reporte ->
                        reporte.titulo.contains(query, ignoreCase = true) ||
                        reporte.descripcion.contains(query, ignoreCase = true) ||
                        reporte.nombreIncidencia.contains(query, ignoreCase = true) ||
                        reporte.nombreUsuario.contains(query, ignoreCase = true) ||
                        reporte.ubicacion.orEmpty().contains(query, ignoreCase = true)
                    }
                }
            }.collect { filtered ->
                _filteredReportes.value = filtered
            }
        }
    }

    fun cargarReportes(idUsuario: Int) {
        currentUserId = idUsuario
        viewModelScope.launch {
            _uiState.value = ReportesAdminUIState.Loading
            try {
                val reportes = getReportesAdminUseCase(idUsuario)
                _allReportes.value = reportes
                _uiState.value = ReportesAdminUIState.Success(reportes)
            } catch (e: Exception) {
                _uiState.value = ReportesAdminUIState.Error(
                    e.localizedMessage ?: "Error al cargar reportes"
                )
            }
        }
    }

    fun cambiarFiltro(filtro: EstadoFiltro) {
        _filtroActual.value = filtro
    }

    fun actualizarBusqueda(query: String) {
        _searchQuery.value = query
    }

    fun reintentar() {
        if (currentUserId != -1) {
            cargarReportes(currentUserId)
        }
    }
}
