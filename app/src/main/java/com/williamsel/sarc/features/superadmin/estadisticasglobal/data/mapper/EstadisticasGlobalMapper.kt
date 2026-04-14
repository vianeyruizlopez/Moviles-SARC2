package com.williamsel.sarc.features.superadmin.estadisticasglobal.data.mapper

import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.models.*
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.*

fun EstadisticasGlobalDto.toDomain() = EstadisticasGlobal(
    totalReportes        = totalReportes,
    ciudadanos           = ciudadanos,
    reportes             = reportes,
    adminsActivos        = adminsActivos,
    reportesPorCategoria = porCategoria.map { it.toDomain() },
    reportesPorEstado    = porEstado.map { it.toDomain() },
    tendencia30Dias      = tendencia30Dias.map { TendenciaDia(it.dia, it.cantidad) },
    totalUltimos30Dias   = total30Dias,
    promedioUltimos30Dias = promedio30Dias,
    distribucion         = distribucion.map {
        DistribucionItem(it.categoria, it.pendiente, it.enProceso, it.resuelto)
    },
    tiempoPromedioHoras  = tiempoPromedioHoras,
    tiemposPorCategoria  = tiemposPorCategoria.map {
        TiempoCategoria(it.categoria, it.emoji, it.horas)
    },
    reportesActivos      = reportesActivos
)

fun CategoriaStatsDto.toDomain() = CategoriaStats(nombre, cantidad, porcentaje)
fun EstadoStatsDto.toDomain()    = EstadoStats(nombre, cantidad, porcentaje)
