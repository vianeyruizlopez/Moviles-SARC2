package com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.usecases.GetDetalleReporteAdminUseCase
import com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.screens.DetalleReporteAdminUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleReporteAdminViewModel @Inject constructor(
    private val getDetalleReporteAdminUseCase: GetDetalleReporteAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleReporteAdminUIState>(DetalleReporteAdminUIState.Loading)
    val uiState: StateFlow<DetalleReporteAdminUIState> = _uiState.asStateFlow()

    private var currentReporteId: Int = -1

    fun cargarDetalle(idReporte: Int) {
        currentReporteId = idReporte
        viewModelScope.launch {
            _uiState.value = DetalleReporteAdminUIState.Loading
            try {
                val reporte = getDetalleReporteAdminUseCase(idReporte)
                _uiState.value = DetalleReporteAdminUIState.Success(reporte)
            } catch (e: Exception) {
                _uiState.value = DetalleReporteAdminUIState.Error(
                    e.localizedMessage ?: "Error al cargar el detalle del reporte"
                )
            }
        }
    }

    fun reintentar() {
        if (currentReporteId != -1) {
            cargarDetalle(currentReporteId)
        }
    }
}
