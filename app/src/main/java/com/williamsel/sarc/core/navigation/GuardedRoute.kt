package com.williamsel.sarc.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.williamsel.sarc.core.session.SessionManager

@Composable
fun GuardedRoute(
    requiredRol: String,
    sessionManager: SessionManager,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val rolCrudo = sessionManager.getRol() ?: ""
    
    val rolActual = when (rolCrudo.uppercase()) {
        "1", "ADMINISTRADOR", "ADMIN" -> "Administrador"
        "3", "SUPERADMINISTRADOR", "SUPERADMIN" -> "SuperAdministrador"
        else -> "Ciudadano"
    }

    if (!sessionManager.isLoggedIn()) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.Publico.Login) {
                popUpTo(0) { inclusive = true }
            }
        }
        return
    }

    if (rolActual != requiredRol) {
        LaunchedEffect(Unit) {
            val destino: Any = when (rolActual) {
                "Administrador" -> Routes.Admin.Panel
                "SuperAdministrador" -> Routes.SuperAdmin.Panel
                else -> Routes.Ciudadano.Panel
            }
            
            navController.navigate(destino) {
                popUpTo(0) { inclusive = true }
            }
        }
        return
    }

    content()
}
