package com.williamsel.sarc.core.navigation

sealed class Routes(val route: String) {

    sealed class Publico(route: String) : Routes(route) {
        data object Inicio     : Publico("publico/inicio")
        data object Login      : Publico("publico/login")
        data object Registro   : Publico("publico/registro")
        data object Terminos   : Publico("publico/terminos")
        data object Privacidad : Publico("publico/privacidad")
    }

    sealed class Ciudadano(route: String) : Routes(route) {
        data object Panel        : Ciudadano("ciudadano/panel")
        data object Mapa         : Ciudadano("ciudadano/mapa")
        data object CrearReporte : Ciudadano("ciudadano/crear_reporte")
        data object MisReportes  : Ciudadano("ciudadano/mis_reportes")
    }

    sealed class Admin(route: String) : Routes(route) {
        data object Panel      : Admin("admin/panel")
        data object Mapa       : Admin("admin/mapa")
        data object Reportes   : Admin("admin/reportes")
        data object EstadoRepo : Admin("admin/estado_reportes")
    }

    sealed class SuperAdmin(route: String) : Routes(route) {
        data object Panel          : SuperAdmin("super_admin/panel")
        data object TodosUsuarios  : SuperAdmin("super_admin/todos_usuarios")
        data object CrearAdmin     : SuperAdmin("super_admin/crear_admin")
        data object Estadisticas   : SuperAdmin("super_admin/estadisticas")
    }
}