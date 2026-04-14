package com.williamsel.sarc.features.superadmin.crearusuario.presentacion.viewmodels

import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.Administrador

data class CrearUsuarioUiState(
    // ── Lista ─────────────────────────────────────────────────────────────────
    val administradores: List<Administrador> = emptyList(),
    val isLoadingLista: Boolean              = false,   // activa el Lottie

    // ── Formulario ────────────────────────────────────────────────────────────
    val nombreCompleto: String  = "",   // "Nombre Apellido1 Apellido2" — se parsea al guardar
    val email: String           = "",
    val contrasena: String      = "",
    val mostrarFormulario: Boolean = false,

    // ── Acción ────────────────────────────────────────────────────────────────
    val isCreando: Boolean      = false,
    val creadoExitoso: Boolean  = false,
    val errorMessage: String?   = null,

    // ── Validación ────────────────────────────────────────────────────────────
    val errorNombre: String?    = null,
    val errorEmail: String?     = null,
    val errorContrasena: String? = null,

    // ── Confirmación eliminar ─────────────────────────────────────────────────
    val adminAEliminar: Administrador? = null
)
