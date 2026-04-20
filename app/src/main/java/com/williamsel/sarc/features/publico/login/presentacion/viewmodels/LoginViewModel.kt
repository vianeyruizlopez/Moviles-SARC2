package com.williamsel.sarc.features.publico.login.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.core.session.SessionManager
import com.williamsel.sarc.features.perfil.domain.usecases.SyncPerfilUseCase // <-- Importante
import com.williamsel.sarc.features.publico.login.domain.usecases.LoginUseCase
import com.williamsel.sarc.features.publico.login.domain.usecases.RestaurarSesionUseCase
import com.williamsel.sarc.features.publico.login.presentacion.screens.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val restaurarSesionUseCase: RestaurarSesionUseCase,
    private val syncPerfilUseCase: SyncPerfilUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        verificarSesionActiva()
    }

    private fun verificarSesionActiva() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val resultado = restaurarSesionUseCase()

            if (resultado != null) {
                // Si ya había sesión, solo navegamos (los datos ya deberían estar en Room)
                guardarSesionYNavegar(resultado.token, resultado.rol, resultado.id)
            } else {
                _uiState.update { it.copy(isLoading = false, sessionChecked = true) }
            }
        }
    }

    fun onCorreoChange(value: String) = _uiState.update { it.copy(correo = value, errorMessage = null) }
    fun onContrasenaChange(value: String) = _uiState.update { it.copy(contrasena = value, errorMessage = null) }

    fun login() {
        val correo = _uiState.value.correo.trim()
        val contrasena = _uiState.value.contrasena

        if (correo.isBlank() || contrasena.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val loginResult = loginUseCase(correo, contrasena)

            // Dentro de la función login() en LoginViewModel.kt
            if (loginResult != null) {
                sessionManager.saveSession(loginResult.token, loginResult.rol, loginResult.id)

                // CORRECCIÓN: Pasar el id que viene del login
                val syncExitoso = syncPerfilUseCase(loginResult.id)

                if (syncExitoso) {
                    guardarSesionYNavegar(loginResult.token, loginResult.rol, loginResult.id)
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Error al sincronizar datos locales")
                    }
                }
            }
        }
    }

    private fun guardarSesionYNavegar(token: String, rol: String, idUsuario: Int) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isSuccess = true,
                rol = rol,
                sessionChecked = true
            )
        }
    }
}