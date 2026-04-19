package com.williamsel.sarc.features.ciudadano.detallereporteciu.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.usecases.GetIddetallereporteciuUseCase
import com.williamsel.sarc.features.ciudadano.detallereporteciu.presentacion.screens.DetallereporteciuUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallereporteciuViewModel @Inject constructor(
    private val getDetalle: GetIddetallereporteciuUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetallereporteciuUIState>(DetallereporteciuUIState.Loading)
    val uiState: StateFlow<DetallereporteciuUIState> = _uiState.asStateFlow()

    fun cargar(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetallereporteciuUIState.Loading
            getDetalle(id)
                .onSuccess { _uiState.value = DetallereporteciuUIState.Success(it) }
                .onFailure { _uiState.value = DetallereporteciuUIState.Error(it.message ?: "Error desconocido") }
        }
    }
}
