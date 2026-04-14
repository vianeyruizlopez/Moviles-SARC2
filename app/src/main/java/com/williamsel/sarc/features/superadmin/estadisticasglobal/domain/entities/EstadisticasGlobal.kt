package com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities

data class EstadisticasGlobal(
    // ── Tarjetas resumen ──────────────────────────────────────────────────────
    val totalReportes: Int,
    val ciudadanos: Int,
    val reportes: Int,                  // reportes del periodo
    val adminsActivos: Int,

    // ── Por categoría ─────────────────────────────────────────────────────────
    val reportesPorCategoria: List<CategoriaStats>,

    // ── Por estado ────────────────────────────────────────────────────────────
    val reportesPorEstado: List<EstadoStats>,

    // ── Tendencia 30 días ─────────────────────────────────────────────────────
    val tendencia30Dias: List<TendenciaDia>,
    val totalUltimos30Dias: Int,
    val promedioUltimos30Dias: Double,

    // ── Distribución por categoría y estado ───────────────────────────────────
    val distribucion: List<DistribucionItem>,

    // ── Tiempo promedio resolución ────────────────────────────────────────────
    val tiempoPromedioHoras: Int,
    val tiemposPorCategoria: List<TiempoCategoria>,
    val reportesActivos: Int
)

data class CategoriaStats(
    val nombre: String,
    val cantidad: Int,
    val porcentaje: Float
)

data class EstadoStats(
    val nombre: String,       // Pendiente, En Proceso, Resuelto
    val cantidad: Int,
    val porcentaje: Float
)

data class TendenciaDia(
    val dia: Int,             // 1..30
    val cantidad: Int
)

data class DistribucionItem(
    val categoria: String,
    val pendiente: Int,
    val enProceso: Int,
    val resuelto: Int
)

data class TiempoCategoria(
    val categoria: String,
    val emoji: String,
    val horas: Int
)
