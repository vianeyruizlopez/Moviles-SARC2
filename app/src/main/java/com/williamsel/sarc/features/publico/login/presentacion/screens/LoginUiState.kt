package com.williamsel.sarc.features.publico.login.presentacion.screens

data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val rol: String? = null,
    val errorMessage: String? = null,
    val sessionChecked: Boolean = false
)
