package com.williamsel.sarc.core.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    sealed class Publico {
        @Serializable data object Inicio
        @Serializable data object Login
        @Serializable data object Registro
        @Serializable data object Terminos
        @Serializable data object Privacidad
    }

    @Serializable
    sealed class Ciudadano {
        @Serializable data object Panel
        @Serializable data object Mapa
        @Serializable data object CrearReporte
        @Serializable data object MisReportes
        @Serializable data class DetalleReporte(val id: Int)
    }

    @Serializable
    sealed class Admin {
        @Serializable data object Panel
        @Serializable data object Mapa
        @Serializable data object Reportes
        @Serializable data object EstadoRepo
    }

    @Serializable
    sealed class SuperAdmin {
        @Serializable data object Panel
        @Serializable data object TodosUsuarios
        @Serializable data object CrearAdmin
        @Serializable data object Estadisticas
    }
}
