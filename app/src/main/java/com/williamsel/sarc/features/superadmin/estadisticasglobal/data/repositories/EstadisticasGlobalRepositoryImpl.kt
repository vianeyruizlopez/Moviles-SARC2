package com.williamsel.sarc.features.superadmin.estadisticasglobal.data.repositories

import com.williamsel.sarc.core.database.dao.ReporteDao
import com.williamsel.sarc.core.database.dao.UsuarioDao
import com.williamsel.sarc.core.database.dao.CatIncidenciasDao
import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.datasource.api.EstadisticasGlobalApi
import com.williamsel.sarc.features.superadmin.estadisticasglobal.data.mapper.toDomain
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.*
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.repositories.EstadisticasGlobalRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EstadisticasGlobalRepositoryImpl @Inject constructor(
    private val api: EstadisticasGlobalApi,
    private val reporteDao: ReporteDao,
    private val usuarioDao: UsuarioDao,
    private val catIncidenciasDao: CatIncidenciasDao
) : EstadisticasGlobalRepository {

    override suspend fun getEstadisticas(): EstadisticasGlobal {
        return try {
            api.getEstadisticas().toDomain()
        } catch (e: Exception) {
            calcularDesdeRoom()
        }
    }

    // ── Fallback: calcula métricas desde Room ─────────────────────────────────
    private suspend fun calcularDesdeRoom(): EstadisticasGlobal {
        val reportes    = reporteDao.getAll().first()
        val usuarios    = usuarioDao.getAll().first()
        val categorias  = catIncidenciasDao.getAll().first()

        val total       = reportes.size
        val ciudadanos  = usuarios.count { it.idRol == 1 }
        val admins      = usuarios.count { it.idRol == 2 }

        // Por categoría
        val porCategoria = categorias.map { cat ->
            val cant = reportes.count { it.idIncidencia == cat.idIncidencia }
            CategoriaStats(
                nombre     = cat.nombre,
                cantidad   = cant,
                porcentaje = if (total > 0) cant * 100f / total else 0f
            )
        }.filter { it.cantidad > 0 }

        // Por estado: 1=Pendiente, 2=En Proceso, 3=Resuelto
        val estadosMap = mapOf(1 to "Pendiente", 2 to "En Proceso", 3 to "Resuelto")
        val porEstado = estadosMap.map { (idEstado, nombre) ->
            val cant = reportes.count { it.idEstado == idEstado }
            EstadoStats(
                nombre     = nombre,
                cantidad   = cant,
                porcentaje = if (total > 0) cant * 100f / total else 0f
            )
        }.filter { it.cantidad > 0 }

        // Tendencia últimos 30 días (usando fechaReporte como timestamp)
        val hace30 = System.currentTimeMillis() - 30L * 24 * 3600 * 1000
        val recientes = reportes.filter { (it.fechaReporte ?: 0L) >= hace30 }
        val tendencia = (1..30).map { dia ->
            val inicio = hace30 + (dia - 1) * 24L * 3600 * 1000
            val fin    = inicio + 24L * 3600 * 1000
            TendenciaDia(
                dia      = dia,
                cantidad = recientes.count { r ->
                    val f = r.fechaReporte ?: 0L; f in inicio until fin
                }
            )
        }

        // Distribución por categoría y estado
        val distribucion = categorias.map { cat ->
            DistribucionItem(
                categoria = cat.nombre,
                pendiente = reportes.count { it.idIncidencia == cat.idIncidencia && it.idEstado == 1 },
                enProceso = reportes.count { it.idIncidencia == cat.idIncidencia && it.idEstado == 2 },
                resuelto  = reportes.count { it.idIncidencia == cat.idIncidencia && it.idEstado == 3 }
            )
        }.filter { it.pendiente + it.enProceso + it.resuelto > 0 }

        // Tiempo promedio (sin datos reales de resolución, se devuelve 0)
        val tiempos = categorias.map { cat ->
            TiempoCategoria(
                categoria = cat.nombre,
                emoji     = when (cat.nombre.lowercase()) {
                    "bache"     -> "🚧"
                    "alumbrado" -> "💡"
                    "basura"    -> "🗑️"
                    else        -> "📋"
                },
                horas = 0
            )
        }

        return EstadisticasGlobal(
            totalReportes         = total,
            ciudadanos            = ciudadanos,
            reportes              = recientes.size,
            adminsActivos         = admins,
            reportesPorCategoria  = porCategoria,
            reportesPorEstado     = porEstado,
            tendencia30Dias       = tendencia,
            totalUltimos30Dias    = recientes.size,
            promedioUltimos30Dias = if (recientes.isEmpty()) 0.0 else recientes.size / 30.0,
            distribucion          = distribucion,
            tiempoPromedioHoras   = 0,
            tiemposPorCategoria   = tiempos,
            reportesActivos       = reportes.count { it.idEstado == 1 || it.idEstado == 2 }
        )
    }
}
