package com.williamsel.sarc.features.ciudadano.registro.presentacion.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.core.session.SessionManager
import com.williamsel.sarc.features.publico.login.data.datasource.GoogleAuthUiClient
import com.williamsel.sarc.features.publico.login.data.datasource.GoogleSignInResult
import com.williamsel.sarc.features.ciudadano.registro.domain.usecases.RegistrarConGoogleUseCase
import com.williamsel.sarc.features.ciudadano.registro.domain.usecases.RegistrarUseCase
import com.williamsel.sarc.features.ciudadano.registro.presentacion.screens.RegistroUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val registrarUseCase: RegistrarUseCase,
    private val registrarConGoogleUseCase: RegistrarConGoogleUseCase,
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()
    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, errorNombre = null, errorMessage = null) }
    }

    fun onPrimerApellidoChange(value: String) {
        _uiState.update { it.copy(primerApellido = value, errorPrimerApellido = null, errorMessage = null) }
    }

    fun onSegundoApellidoChange(value: String) {
        _uiState.update { it.copy(segundoApellido = value, errorMessage = null) }
    }

    fun onEdadChange(value: String) {
        _uiState.update { it.copy(edad = value, errorEdad = null, errorMessage = null) }
    }

    fun onCorreoChange(value: String) {
        _uiState.update { it.copy(correo = value, errorCorreo = null, errorMessage = null) }
    }

    fun onContrasenaChange(value: String) {
        _uiState.update { it.copy(contrasena = value, errorContrasena = null, errorMessage = null) }
    }

    fun onConfirmarContrasenaChange(value: String) {
        _uiState.update { it.copy(confirmarContrasena = value, errorConfirmar = null, errorMessage = null) }
    }
    fun registrar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val resultado = registrarUseCase(
                nombre = _uiState.value.nombre.trim(),
                primerApellido = _uiState.value.primerApellido.trim(),
                segundoApellido = _uiState.value.segundoApellido.trim(),
                correo = _uiState.value.correo.trim(),
                contrasena = _uiState.value.contrasena,
                confirmarContrasena = _uiState.value.confirmarContrasena,
                edad = _uiState.value.edad.trim()
            )

            resultado.onSuccess { usuario ->
                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true, rol = usuario.rol)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
    fun iniciarRegistroConGoogle(onIntentReady: (intentSender: android.content.IntentSender?) -> Unit) {
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
                it.copy(isGoogleLoading = false, errorMessage = "Registro cancelado")
            }
            return
        }

        viewModelScope.launch {
            when (val result = googleAuthUiClient.signInWithIntent(intent)) {
                is GoogleSignInResult.Success -> {
                    val resultado = registrarConGoogleUseCase(result.idToken)
                    if (resultado != null) {
                        _uiState.update {
                            it.copy(
                                isGoogleLoading = false,
                                isSuccess = true,
                                rol = resultado.rol
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isGoogleLoading = false,
                                errorMessage = "Error al crear la cuenta en el servidor"
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
    private fun validarCampos(): Boolean {
        val state = _uiState.value
        var valido = true

        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = "El nombre es requerido") }
            valido = false
        }

        if (state.correo.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(state.correo).matches()) {
            _uiState.update { it.copy(errorCorreo = "Ingresa un correo válido") }
            valido = false
        }

        if (state.contrasena.length < 6) {
            _uiState.update { it.copy(errorContrasena = "Mínimo 6 caracteres") }
            valido = false
        }

        if (state.contrasena != state.confirmarContrasena) {
            _uiState.update { it.copy(errorConfirmar = "Las contraseñas no coinciden") }
            valido = false
        }

        return valido
    }

    fun resetState() {
        _uiState.update { RegistroUiState() }
    }
}
