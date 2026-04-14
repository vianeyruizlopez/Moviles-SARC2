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
    val rolActual = sessionManager.getRol()

    if (!sessionManager.isLoggedIn()) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.Publico.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
        return
    }

    if (rolActual != requiredRol) {
        LaunchedEffect(Unit) {
            val destino = if (rolActual == "Administrador")
                Routes.Admin.Panel.route
            else
                Routes.Ciudadano.Panel.route
            navController.navigate(destino) { popUpTo(0) { inclusive = true } }
        }
        return
    }

    content()
}