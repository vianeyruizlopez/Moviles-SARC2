package com.williamsel.sarc.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
import com.williamsel.sarc.features.ciudadano.detallereporteciu.presentacion.screens.DetallereporteciuScreen

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
    val startDestination: Any = when {
        !sessionManager.isLoggedIn()                     -> Routes.Publico.Inicio
        sessionManager.getRol() == "SuperAdministrador" -> Routes.SuperAdmin.Panel
        sessionManager.getRol() == "Administrador"      -> Routes.Admin.Panel
        else                                            -> Routes.Ciudadano.Panel
    }

    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {

        composable<Routes.Publico.Inicio> {
            InicioScreen(
                onIniciarSesion = { navController.navigate(Routes.Publico.Login) },
                onCrearCuenta   = { navController.navigate(Routes.Publico.Registro) },
                onTerminos      = { navController.navigate(Routes.Publico.Terminos) },
                onPrivacidad    = { navController.navigate(Routes.Publico.Privacidad) }
            )
        }

        composable<Routes.Publico.Login> {
            LoginScreen(
                onLoginSuccess = { rol ->
                    val destino: Any = when (rol) {
                        "SuperAdministrador" -> Routes.SuperAdmin.Panel
                        "Administrador"      -> Routes.Admin.Panel
                        else                 -> Routes.Ciudadano.Panel
                    }
                    navController.navigate(destino) {
                        popUpTo(Routes.Publico.Inicio) { inclusive = true }
                    }
                },
                onCrearCuenta  = { navController.navigate(Routes.Publico.Registro) },
                onVolverInicio = { navController.popBackStack() }
            )
        }

        composable<Routes.Publico.Registro> {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Routes.Ciudadano.Panel) {
                        popUpTo(Routes.Publico.Inicio) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.Publico.Terminos> {
            TerminosScreen(onBack = { navController.popBackStack() })
        }

        composable<Routes.Publico.Privacidad> {
            PrivacidadScreen(onBack = { navController.popBackStack() })
        }


        composable<Routes.Ciudadano.Panel> {
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelCiudadanoScreen(
                    onNavigateToMapa         = { navController.navigate(Routes.Ciudadano.Mapa) },
                    onNavigateToCrearReporte = { navController.navigate(Routes.Ciudadano.CrearReporte) },
                    onNavigateToMisReportes  = { navController.navigate(Routes.Ciudadano.MisReportes) },
                    onLogout = {
                        sessionManager.clearSession()
                        navController.navigate(Routes.Publico.Inicio) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable<Routes.Ciudadano.Mapa> {
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                MapaCiudadanoScreen(onBack = { navController.popBackStack() })
            }
        }

        composable<Routes.Ciudadano.CrearReporte> {
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

        composable<Routes.Ciudadano.MisReportes> {
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                MisReportesScreen(
                    onBack       = { navController.popBackStack() },
                    onVerDetalle = { id -> 
                        navController.navigate(Routes.Ciudadano.DetalleReporte(id = id)) 
                    }
                )
            }
        }

        composable<Routes.Ciudadano.DetalleReporte> { backStackEntry ->
            val route: Routes.Ciudadano.DetalleReporte = backStackEntry.toRoute()
            GuardedRoute(
                requiredRol    = "Ciudadano",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                DetallereporteciuScreen(
                    reporteId = route.id.toString(),
                    onBack    = { navController.popBackStack() }
                )
            }
        }


        composable<Routes.Admin.Panel> {
            GuardedRoute(
                requiredRol    = "Administrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelPrincipalAdminScreen(
                    onNavegaMapa       = { navController.navigate(Routes.Admin.Mapa) },
                    onNavegaReportes   = { navController.navigate(Routes.Admin.Reportes) },
                    onNavegaAdminPanel = { navController.navigate(Routes.Admin.EstadoRepo) },
                    onSalir = {
                        sessionManager.clearSession()
                        navController.navigate(Routes.Publico.Inicio) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable<Routes.Admin.Mapa> {
            GuardedRoute(
                requiredRol    = "Administrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                MapaAdminScreen(onVolver = { navController.popBackStack() })
            }
        }

        composable<Routes.Admin.Reportes> {
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

        composable<Routes.Admin.EstadoRepo> {
            GuardedRoute(
                requiredRol    = "Administrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelDeAdminScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onVerDetalle   = { id ->
                    }
                )
            }
        }


        composable<Routes.SuperAdmin.Panel> {
            GuardedRoute(
                requiredRol    = "SuperAdministrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                PanelSuperAdminScreen(
                    onNavigateToUsuarios     = { navController.navigate(Routes.SuperAdmin.TodosUsuarios) },
                    onNavigateToCrearAdmin   = { navController.navigate(Routes.SuperAdmin.CrearAdmin) },
                    onNavigateToEstadisticas = { navController.navigate(Routes.SuperAdmin.Estadisticas) },
                    onLogout = {
                        sessionManager.clearSession()
                        navController.navigate(Routes.Publico.Inicio) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable<Routes.SuperAdmin.TodosUsuarios> {
            GuardedRoute(
                requiredRol    = "SuperAdministrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                UsuarioSuperAdminScreen(onBack = { navController.popBackStack() })
            }
        }

        composable<Routes.SuperAdmin.CrearAdmin> {
            GuardedRoute(
                requiredRol    = "SuperAdministrador",
                sessionManager = sessionManager,
                navController  = navController
            ) {
                CrearUsuarioScreen(onBack = { navController.popBackStack() })
            }
        }

        composable<Routes.SuperAdmin.Estadisticas> {
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
