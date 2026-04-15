package com.williamsel.sarc.features.ciudadano.registro.presentacion.screens

data class RegistroUiState(
    val nombre: String = "",
    val primerApellido: String = "",
    val segundoApellido: String = "",
    val correo: String = "",
    val edad: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val rol: String? = null,
    val errorMessage: String? = null,

    val errorNombre: String? = null,
    val errorPrimerApellido: String? = null,
    val errorCorreo: String? = null,
    val errorEdad: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmar: String? = null
)