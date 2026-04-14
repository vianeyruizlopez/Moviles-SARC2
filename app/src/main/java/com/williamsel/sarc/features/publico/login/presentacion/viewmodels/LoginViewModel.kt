package com.williamsel.sarc.features.publico.login.presentacion.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.core.session.SessionManager
import com.williamsel.sarc.features.publico.login.data.datasource.GoogleAuthUiClient
import com.williamsel.sarc.features.publico.login.data.datasource.GoogleSignInResult
import com.williamsel.sarc.features.publico.login.domain.usecases.LoginConGoogleUseCase
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
    private val loginConGoogleUseCase: LoginConGoogleUseCase,
    private val restaurarSesionUseCase: RestaurarSesionUseCase,
    private val googleAuthUiClient: GoogleAuthUiClient,
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
                guardarSesionYNavegar(resultado.token, resultado.rol)
            } else {
                _uiState.update { it.copy(isLoading = false, sessionChecked = true) }
            }
        }
    }

    fun onCorreoChange(value: String) {
        _uiState.update { it.copy(correo = value, errorMessage = null) }
    }

    fun onContrasenaChange(value: String) {
        _uiState.update { it.copy(contrasena = value, errorMessage = null) }
    }

    fun login() {
        val correo = _uiState.value.correo.trim()
        val contrasena = _uiState.value.contrasena

        if (correo.isBlank() || contrasena.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val resultado = loginUseCase(correo, contrasena)
            if (resultado != null) {
                guardarSesionYNavegar(resultado.token, resultado.rol)
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Correo o contraseña incorrectos")
                }
            }
        }
    }

    fun iniciarLoginConGoogle(onIntentReady: (intent: android.content.IntentSender?) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGoogleLoading = true, errorMessage = null) }
            val request = googleAuthUiClient.beginSignIn()
            onIntentReady(request?.intentSender)
            if (request == null) {
                _uiState.update {
                    it.copy(
                        isGoogleLoading = false,
                        errorMessage = "No se encontraron cuentas de Google en el dispositivo"
                    )
                }
            }
        }
    }
    fun onGoogleSignInResult(intent: Intent?) {
        if (intent == null) {
            _uiState.update {
                it.copy(isGoogleLoading = false, errorMessage = "Inicio de sesión cancelado")
            }
            return
        }

        viewModelScope.launch {
            when (val result = googleAuthUiClient.signInWithIntent(intent)) {
                is GoogleSignInResult.Success -> {
                    val loginResult = loginConGoogleUseCase(result.idToken)
                    if (loginResult != null) {
                        guardarSesionYNavegar(loginResult.token, loginResult.rol)
                    } else {
                        _uiState.update {
                            it.copy(
                                isGoogleLoading = false,
                                errorMessage = "Error al validar sesión en el servidor"
                            )
                        }
                    }
                }
                is GoogleSignInResult.Error -> {
                    _uiState.update {
                        it.copy(isGoogleLoading = false, errorMessage = result.mensaje)
                    }
                }
            }
        }
    }

    private fun guardarSesionYNavegar(token: String, rol: String) {
        sessionManager.saveSession(token = token, rol = rol)
        _uiState.update {
            it.copy(
                isLoading = false,
                isGoogleLoading = false,
                isSuccess = true,
                rol = rol,
                sessionChecked = true
            )
        }
    }
}
