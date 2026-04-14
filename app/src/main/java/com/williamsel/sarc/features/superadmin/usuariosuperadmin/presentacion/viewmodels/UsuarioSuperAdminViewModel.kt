package com.williamsel.sarc.features.superadmin.usuariosuperadmin.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.UsuarioSistema
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.usecases.GetResumenUsuariosUseCase
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.usecases.ToggleActivoUsuarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioSuperAdminViewModel @Inject constructor(
    private val getResumenUsuariosUseCase: GetResumenUsuariosUseCase,
    private val toggleActivoUsuarioUseCase: ToggleActivoUsuarioUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioSuperAdminUiState())
    val uiState: StateFlow<UsuarioSuperAdminUiState> = _uiState.asStateFlow()

    init { cargar() }

    private fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getResumenUsuariosUseCase()
                .catch {
                    _uiState.update { s ->
                        s.copy(isLoading = false, errorMessage = "No se pudieron cargar los usuarios.")
                    }
                }
                .collect { resumen ->
                    _uiState.update {
                        it.copy(
                            isLoading            = false,
                            totalUsuarios        = resumen.totalUsuarios,
                            totalAdministradores = resumen.totalAdministradores,
                            totalCiudadanos      = resumen.totalCiudadanos,
                            administradores      = resumen.administradores,
                            ciudadanos           = resumen.ciudadanos
                        )
                    }
                }
        }
    }

    fun toggleActivo(usuario: UsuarioSistema) {
        viewModelScope.launch {
            toggleActivoUsuarioUseCase(usuario.idUsuario, !usuario.activo).onFailure {
                _uiState.update { s ->
                    s.copy(errorMessage = "No se pudo cambiar el estado del usuario.")
                }
            }
        }
    }

    fun clearError() { _uiState.update { it.copy(errorMessage = null) } }
}
