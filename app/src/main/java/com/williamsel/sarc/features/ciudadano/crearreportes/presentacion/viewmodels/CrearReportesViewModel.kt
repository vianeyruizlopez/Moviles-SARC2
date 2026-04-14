package com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.NuevoReporte
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases.EnviarReporteUseCase
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases.GetCategoriasUseCase
import com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.screens.CrearReportesUiState
import com.williamsel.sarc.core.hardware.domain.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class CrearReportesViewModel @Inject constructor(
    private val enviarReporteUseCase: EnviarReporteUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearReportesUiState())
    val uiState: StateFlow<CrearReportesUiState> = _uiState.asStateFlow()

    init { cargarCategorias() }

    private fun cargarCategorias() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCargandoCategorias = true) }
            try {
                val cats = getCategoriasUseCase()
                _uiState.update { it.copy(categorias = cats, isCargandoCategorias = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCargandoCategorias = false,
                        categorias = listOf(
                            CategoriaIncidencia(1, "Bache",      "🚧"),
                            CategoriaIncidencia(2, "Basura",     "🗑️"),
                            CategoriaIncidencia(3, "Alumbrado",  "💡"),
                            CategoriaIncidencia(4, "Otro",       "📋")
                        )
                    )
                }
            }
        }
    }

    fun onTituloChange(value: String) {
        _uiState.update { it.copy(titulo = value, errorTitulo = null) }
    }

    fun onDescripcionChange(value: String) {
        _uiState.update { it.copy(descripcion = value, errorDescripcion = null) }
    }

    fun onCategoriaSeleccionada(categoria: CategoriaIncidencia) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria, errorCategoria = null) }
    }

    fun onImagenSeleccionada(bitmap: Bitmap) {
        _uiState.update { it.copy(imagen = bitmap) }
    }

    fun onImagenEliminada() {
        _uiState.update { it.copy(imagen = null) }
    }

    fun obtenerUbicacion() {
        viewModelScope.launch {
            try {
                val ubicacionModel = locationManager.obtenerUbicacion()
                ubicacionModel?.let { ub ->
                    _uiState.update {
                        it.copy(
                            ubicacion      = ub.direccion ?: "${ub.latitud}, ${ub.longitud}",
                            latitud        = ub.latitud,
                            longitud       = ub.longitud,
                            errorUbicacion = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorUbicacion = "No se pudo obtener la ubicación")
                }
            }
        }
    }

    fun enviarReporte(idUsuario: Int) {
        if (!validar()) return

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val imagenBytes = state.imagen?.let { bitmap ->
                ByteArrayOutputStream().also { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                }.toByteArray()
            }

            val resultado = enviarReporteUseCase(
                idUsuario = idUsuario,
                reporte   = NuevoReporte(
                    titulo       = state.titulo.trim(),
                    descripcion  = state.descripcion.trim(),
                    ubicacion    = state.ubicacion.trim(),
                    latitud      = state.latitud,
                    longitud     = state.longitud,
                    idIncidencia = state.categoriaSeleccionada!!.id,
                    imagenBytes  = imagenBytes
                )
            )

            resultado.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isEnviado = true) } },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading    = false,
                            errorMessage = "Error al enviar el reporte. Fue guardado localmente."
                        )
                    }
                }
            )
        }
    }

    private fun validar(): Boolean {
        val state = _uiState.value
        var valido = true

        if (state.titulo.isBlank()) {
            _uiState.update { it.copy(errorTitulo = "El título es obligatorio") }
            valido = false
        }
        if (state.descripcion.isBlank()) {
            _uiState.update { it.copy(errorDescripcion = "La descripción es obligatoria") }
            valido = false
        }
        if (state.categoriaSeleccionada == null) {
            _uiState.update { it.copy(errorCategoria = "Selecciona una categoría") }
            valido = false
        }
        if (state.ubicacion.isBlank()) {
            _uiState.update { it.copy(errorUbicacion = "La ubicación es obligatoria") }
            valido = false
        }
        return valido
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetEnviado() {
        _uiState.update { it.copy(isEnviado = false) }
    }
}
