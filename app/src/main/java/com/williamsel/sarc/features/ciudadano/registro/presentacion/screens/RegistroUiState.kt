package com.williamsel.sarc.features.publico.registro.presentacion.screens

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val rol: String? = null,
    val errorMessage: String? = null,
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmar: String? = null
)
