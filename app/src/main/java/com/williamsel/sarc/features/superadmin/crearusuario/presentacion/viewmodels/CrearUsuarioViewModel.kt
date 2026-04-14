package com.williamsel.sarc.features.superadmin.crearusuario.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.Administrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.NuevoAdministrador
import com.williamsel.sarc.features.superadmin.crearusuario.domain.usecases.CrearAdministradorUseCase
import com.williamsel.sarc.features.superadmin.crearusuario.domain.usecases.EliminarAdministradorUseCase
import com.williamsel.sarc.features.superadmin.crearusuario.domain.usecases.GetAdministradoresUseCase
import com.williamsel.sarc.features.superadmin.crearusuario.domain.usecases.ToggleActivoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearUsuarioViewModel @Inject constructor(
    private val crearAdministradorUseCase: CrearAdministradorUseCase,
    private val getAdministradoresUseCase: GetAdministradoresUseCase,
    private val toggleActivoUseCase: ToggleActivoUseCase,
    private val eliminarAdministradorUseCase: EliminarAdministradorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearUsuarioUiState())
    val uiState: StateFlow<CrearUsuarioUiState> = _uiState.asStateFlow()

    init { cargarAdministradores() }

    // ── Lista ─────────────────────────────────────────────────────────────────
    private fun cargarAdministradores() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLista = true) }
            getAdministradoresUseCase()
                .catch { _uiState.update { s -> s.copy(isLoadingLista = false) } }
                .collect { lista ->
                    _uiState.update { it.copy(administradores = lista, isLoadingLista = false) }
                }
        }
    }

    // ── Form handlers ─────────────────────────────────────────────────────────
    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombreCompleto = value, errorNombre = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorEmail = null) }
    }

    fun onContrasenaChange(value: String) {
        _uiState.update { it.copy(contrasena = value, errorContrasena = null) }
    }

    fun mostrarFormulario() {
        _uiState.update { it.copy(mostrarFormulario = true) }
    }

    fun ocultarFormulario() {
        _uiState.update {
            it.copy(
                mostrarFormulario = false,
                nombreCompleto    = "",
                email             = "",
                contrasena        = "",
                errorNombre       = null,
                errorEmail        = null,
                errorContrasena   = null
            )
        }
    }

    // ── Crear ─────────────────────────────────────────────────────────────────
    fun crearAdministrador() {
        if (!validar()) return

        val state  = _uiState.value
        val partes = state.nombreCompleto.trim().split(" ")
        val nombre          = partes.getOrElse(0) { "" }
        val primerApellido  = partes.getOrElse(1) { "" }
        val segundoApellido = partes.drop(2).joinToString(" ")

        viewModelScope.launch {
            _uiState.update { it.copy(isCreando = true, errorMessage = null) }
            val resultado = crearAdministradorUseCase(
                NuevoAdministrador(
                    nombre          = nombre,
                    primerApellido  = primerApellido,
                    segundoApellido = segundoApellido,
                    email           = state.email.trim(),
                    contrasena      = state.contrasena
                )
            )
            resultado.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isCreando         = false,
                            creadoExitoso     = true,
                            mostrarFormulario = false,
                            nombreCompleto    = "",
                            email             = "",
                            contrasena        = ""
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isCreando    = false,
                            errorMessage = "Error al crear administrador. Intenta de nuevo."
                        )
                    }
                }
            )
        }
    }

    // ── Toggle activo ─────────────────────────────────────────────────────────
    fun toggleActivo(admin: Administrador) {
        viewModelScope.launch {
            toggleActivoUseCase(admin.idUsuario, !admin.activo)
        }
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────
    fun confirmarEliminar(admin: Administrador) {
        _uiState.update { it.copy(adminAEliminar = admin) }
    }

    fun cancelarEliminar() {
        _uiState.update { it.copy(adminAEliminar = null) }
    }

    fun eliminarAdministrador() {
        val admin = _uiState.value.adminAEliminar ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(adminAEliminar = null) }
            eliminarAdministradorUseCase(admin.idUsuario).onFailure {
                _uiState.update { s ->
                    s.copy(errorMessage = "No se pudo eliminar el administrador.")
                }
            }
        }
    }

    // ── Validación ────────────────────────────────────────────────────────────
    private fun validar(): Boolean {
        val state = _uiState.value
        var valido = true
        if (state.nombreCompleto.isBlank()) {
            _uiState.update { it.copy(errorNombre = "El nombre es obligatorio") }
            valido = false
        }
        if (state.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(errorEmail = "Ingresa un email válido") }
            valido = false
        }
        if (state.contrasena.length < 6) {
            _uiState.update { it.copy(errorContrasena = "Mínimo 6 caracteres") }
            valido = false
        }
        return valido
    }

    fun clearError() { _uiState.update { it.copy(errorMessage = null) } }
    fun clearExito() { _uiState.update { it.copy(creadoExitoso = false) } }
}
