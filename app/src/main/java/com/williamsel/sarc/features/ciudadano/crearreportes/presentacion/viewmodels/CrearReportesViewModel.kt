package com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.core.hardware.domain.NetworkManager
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.NuevoReporte
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases.EnviarReporteUseCase
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases.GetCategoriasUseCase
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.usecases.ObtenerUbicacionUseCase
import com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.screens.CrearReportesUiState
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
    private val obtenerUbicacionUseCase: ObtenerUbicacionUseCase,
    private val networkManager: NetworkManager ///el internet entra como hadware
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
                _uiState.update { it.copy(isCargandoCategorias = false) }
            }
        }
    }

    fun onTituloChange(value: String) = _uiState.update { it.copy(titulo = value, errorTitulo = null) }
    fun onDescripcionChange(value: String) = _uiState.update { it.copy(descripcion = value, errorDescripcion = null) }
    fun onUbicacionChange(value: String) = _uiState.update { it.copy(ubicacion = value, errorUbicacion = null) }
    fun onCategoriaSeleccionada(categoria: CategoriaIncidencia) = _uiState.update { it.copy(categoriaSeleccionada = categoria, errorCategoria = null) }
    fun onImagenSeleccionada(bitmap: Bitmap) = _uiState.update { it.copy(imagen = bitmap) }
    fun onImagenEliminada() = _uiState.update { it.copy(imagen = null) }

    fun obtenerUbicacion() {
        viewModelScope.launch {
            val ub = obtenerUbicacionUseCase()
            if (ub != null) {
                _uiState.update {
                    it.copy(
                        ubicacion = ub.direccion ?: "${ub.latitud}, ${ub.longitud}",
                        latitud = ub.latitud,
                        longitud = ub.longitud,
                        errorUbicacion = null
                    )
                }
            } else {
                _uiState.update { it.copy(errorUbicacion = "Error al obtener GPS") }
            }
        }
    }

    fun enviarReporte(idUsuario: Int) {
        if (!validar()) return

        // Validación de Hardware de Red
        if (!networkManager.isNetworkAvailable()) {
            _uiState.update { it.copy(errorMessage = "Sin conexión a internet") }
            return
        }

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val imagenBytes = state.imagen?.let { bitmap ->
                ByteArrayOutputStream().also { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                }.toByteArray()
            }

            val result = enviarReporteUseCase(
                idUsuario = idUsuario,
                reporte = NuevoReporte(
                    titulo = state.titulo,
                    descripcion = state.descripcion,
                    ubicacion = state.ubicacion,
                    latitud = state.latitud,
                    longitud = state.longitud,
                    idIncidencia = state.categoriaSeleccionada!!.id,
                    imagenBytes = imagenBytes
                )
            )

            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isEnviado = true) } },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
            )
        }
    }

    private fun validar(): Boolean {
        val state = _uiState.value
        var isOk = true
        if (state.titulo.isBlank()) {
            _uiState.update { it.copy(errorTitulo = "Obligatorio") }
            isOk = false
        }
        if (state.categoriaSeleccionada == null) {
            _uiState.update { it.copy(errorCategoria = "Selecciona categoría") }
            isOk = false
        }
        return isOk
    }

    fun resetEnviado() = _uiState.update { it.copy(isEnviado = false) }
    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
}