package com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.usecases.ActualizarEstadoReporteUseCase
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.usecases.GetDetalleReporteAdminUseCase
import com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.screens.DetalleReporteAdminUIState
import com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.screens.DetalleReporteUIModel
import com.williamsel.sarc.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetalleReporteAdminViewModel @Inject constructor(
    private val getDetalleReporteAdminUseCase: GetDetalleReporteAdminUseCase,
    private val actualizarEstadoReporteUseCase: ActualizarEstadoReporteUseCase
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
                val uiModel = mapToUIModel(reporte)
                _uiState.value = DetalleReporteAdminUIState.Success(uiModel)
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

    fun actualizarEstado(idReporte: Int, idEstado: Int) {
        viewModelScope.launch {
            try {
                actualizarEstadoReporteUseCase(idReporte, idEstado)
                cargarDetalle(idReporte)
            } catch (e: Exception) {
                _uiState.value = DetalleReporteAdminUIState.Error(
                    e.localizedMessage ?: "Error al actualizar el estado"
                )
            }
        }
    }


    private fun mapToUIModel(domain: com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin): DetalleReporteUIModel {
        return DetalleReporteUIModel(
            idReporte = domain.idReporte,
            titulo = domain.titulo,
            descripcion = domain.descripcion,
            nombreUsuario = domain.nombreUsuario,
            nombreIncidencia = domain.nombreIncidencia,
            categoriaEmoji = getCategoriaEmoji(domain.nombreIncidencia),
            nombreEstado = domain.nombreEstado,
            idEstado = domain.idEstado,
            estadoColor = getEstadoColorById(domain.idEstado),
            ubicacion = domain.ubicacion,
            latitud = domain.latitud,
            longitud = domain.longitud,
            imagen = domain.imagen,
            fechaFormateada = formatFechaDetalle(domain.fecha)
        )
    }

    private fun getEstadoColorById(idEstado: Int) = when (idEstado) {
        1    -> OrangeWarning
        2    -> BlueProceso
        3    -> GreenResuelto
        else -> TextMid
    }

    private fun getCategoriaEmoji(nombreIncidencia: String) = when (nombreIncidencia.lowercase()) {
        "bache"       -> "\uD83D\uDEA7"
        "basura"      -> "\uD83D\uDDD1"
        "alumbrado"   -> "\uD83D\uDCA1"
        else          -> "\uD83D\uDCCB"
    }

    private fun formatFechaDetalle(fechaStr: String): String {
        return try {
            val dateTime = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val formatter = DateTimeFormatter.ofPattern(
                "dd 'de' MMMM, yyyy 'a las' HH:mm",
                Locale("es", "MX")
            )
            dateTime.format(formatter)
        } catch (e: Exception) {
            fechaStr
        }
    }
}
