package com.williamsel.sarc.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.williamsel.sarc.core.session.SessionManager

import com.williamsel.sarc.features.publico.incio.presentacion.screens.InicioScreen
import com.williamsel.sarc.features.publico.login.presentacion.screens.LoginScreen
import com.williamsel.sarc.features.ciudadano.registro.presentacion.screens.RegistroScreen
import com.williamsel.sarc.features.publico.terminosycondiciones.presentacion.screens.TerminosScreen
import com.williamsel.sarc.features.publico.politicasdeprivacidad.presentacion.screens.PrivacidadScreen

import com.williamsel.sarc.features.ciudadano.panelciu.presentacion.screens.PanelCiudadanoScreen
import com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.screens.MapaCiudadanoScreen
import com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.screens.CrearReporteScreen
import com.williamsel.sarc.features.ciudadano.misreportesciu.presentacion.screens.MisReportesScreen

import com.williamsel.sarc.features.administrador.paneladmin.presentacion.screens.PanelPrincipalAdminScreen
import com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens.MapaAdminScreen
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens.ReportesAdminScreen
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens.PanelDeAdminScreen

import com.williamsel.sarc.features.superadmin.panelsuperadmin.presentacion.screens.PanelSuperAdminScreen
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.presentacion.screens.UsuarioSuperAdminScreen
import com.williamsel.sarc.features.superadmin.crearusuario.presentacion.screens.CrearUsuarioScreen
import com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.screens.EstadisticasGlobalScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    sessionManager: SessionManager
) {
    val startDestination = when {
        !sessionManager.isLoggedIn()                        -> Routes.Publico.Inicio.route
        sessionManager.getRol() == "SuperAdministrador"    -> Routes.SuperAdmin.Panel.route
        sessionManager.getRol() == "Administrador"         -> Routes.Admin.Panel.route
        sessionManager.getRol() == "Ciudadano"             -> Routes.Ciudadano.Panel.route
        else                                               -> Routes.Publico.Inicio.route
    }

    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {

        composable(Routes.Publico.Inicio.route) {
            InicioScreen(
                onIniciarSesion = { navController.navigate(Routes.Publico.Login.route) },
                onCrearCuenta   = { navController.navigate(Routes.Publico.Registro.route) },
                onTerminos      = { navController.navigate(Routes.Publico.Terminos.route) },
                onPrivacidad    = { navController.navigate(Routes.Publico.Privacidad.route) }
            )
        }

        composable(Routes.Publico.Login.route) {
            LoginScreen(
                onLoginSuccess = { rol ->
                    val destino = when (rol) {
                        "SuperAdministrador" -> Routes.SuperAdmin.Panel.route
                        "Administrador"      -> Routes.Admin.Panel.route
                        else                 -> Routes.Ciudadano.Panel.route
                    }
                    navController.navigate(destino) {
                        popUpTo(Routes.Publico.Inicio.route) { inclusive = true }
                    }
                },
                onCrearCuenta  = { navController.navigate(Routes.Publico.Registro.route) },
                onVolverInicio = { navController.popBackStack() }
            )
        }

        composable(Routes.Publico.Registro.route) {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Routes.Ciudadano.Panel.route) {
                        popUpTo(Routes.Publico.Inicio.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Publico.Terminos.route) {
            TerminosScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.Publico.Privacidad.route) {
            PrivacidadScreen(onBack = { navController.popBackStack() })
        }


        composable(Routes.Ciudadano.Panel.route) {
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelCiudadanoScreen(
                    onNavigateToMapa         = { navController.navigate(Routes.Ciudadano.Mapa.route) },
                    onNavigateToCrearReporte = { navController.navigate(Routes.Ciudadano.CrearReporte.route) },
                    onNavigateToMisReportes  = { navController.navigate(Routes.Ciudadano.MisReportes.route) },
                    onLogout = {
                        sessionManager.clearSession()
                        navController.navigate(Routes.Publico.Inicio.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Routes.Ciudadano.Mapa.route) {
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                MapaCiudadanoScreen(onBack = { navController.popBackStack() })
            }
        }

        composable(Routes.Ciudadano.CrearReporte.route) {
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                CrearReporteScreen(
                    onReporteCreado = { navController.popBackStack() },
                    onBack          = { navController.popBackStack() }
                )
            }
        }

        composable(Routes.Ciudadano.MisReportes.route) {
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                MisReportesScreen(onBack = { navController.popBackStack() })
            }
        }


        composable(Routes.Admin.Panel.route) {
            GuardedRoute(
                requiredRol    = "Administrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelPrincipalAdminScreen(
                    onNavegaMapa       = { navController.navigate(Routes.Admin.Mapa.route) },
                    onNavegaReportes   = { navController.navigate(Routes.Admin.Reportes.route) },
                    onNavegaAdminPanel = { navController.navigate(Routes.Admin.EstadoRepo.route) },
                    onSalir = {
                        sessionManager.clearSession()
                        navController.navigate(Routes.Publico.Inicio.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Routes.Admin.Mapa.route) {
            GuardedRoute(
                requiredRol    = "Administrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                MapaAdminScreen(onVolver = { navController.popBackStack() })
            }
        }

        composable(Routes.Admin.Reportes.route) {
            GuardedRoute(
                requiredRol    = "Administrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                ReportesAdminScreen(
                    idUsuario      = 0,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(Routes.Admin.EstadoRepo.route) {
            GuardedRoute(
                requiredRol    = "Administrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelDeAdminScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onVerDetalle   = { /* TODO: navigate to detalle */ }
                )
            }
        }


        composable(Routes.SuperAdmin.Panel.route) {
            GuardedRoute(
                requiredRol    = "SuperAdministrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelSuperAdminScreen(
                    onNavigateToUsuarios     = { navController.navigate(Routes.SuperAdmin.TodosUsuarios.route) },
                    onNavigateToCrearAdmin   = { navController.navigate(Routes.SuperAdmin.CrearAdmin.route) },
                    onNavigateToEstadisticas = { navController.navigate(Routes.SuperAdmin.Estadisticas.route) },
                    onLogout = {
                        sessionManager.clearSession()
                        navController.navigate(Routes.Publico.Inicio.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Routes.SuperAdmin.TodosUsuarios.route) {
            GuardedRoute(
                requiredRol    = "SuperAdministrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                UsuarioSuperAdminScreen(onBack = { navController.popBackStack() })
            }
        }

        composable(Routes.SuperAdmin.CrearAdmin.route) {
            GuardedRoute(
                requiredRol    = "SuperAdministrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                CrearUsuarioScreen(onBack = { navController.popBackStack() })
            }
        }

        composable(Routes.SuperAdmin.Estadisticas.route) {
            GuardedRoute(
                requiredRol    = "SuperAdministrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                EstadisticasGlobalScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
