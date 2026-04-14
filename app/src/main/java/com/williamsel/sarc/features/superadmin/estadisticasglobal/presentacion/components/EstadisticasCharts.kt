package com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.CategoriaStats
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.DistribucionItem
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.EstadoStats
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.TendenciaDia
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.TextDark
import com.williamsel.sarc.ui.theme.TextMid
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

// ── Colores para los gráficos ─────────────────────────────────────────────────
val coloresGrafico = listOf(
    Color(0xFFF57C00),  // Naranja — Pendiente / Bache
    Color(0xFF1976D2),  // Azul — En Proceso / Alumbrado
    Color(0xFF388E3C),  // Verde — Resuelto / Basura
    Color(0xFF7B1FA2),  // Morado — Otro
    Color(0xFFD32F2F)   // Rojo extra
)

val colorPendiente  = Color(0xFFF57C00)
val colorEnProceso  = Color(0xFF1976D2)
val colorResuelto   = Color(0xFF388E3C)

// ── Gráfico de pastel (Pie Chart) ─────────────────────────────────────────────
@Composable
fun PieChart(
    items: List<Pair<String, Float>>,   // (nombre, porcentaje)
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val radio      = min(size.width, size.height) / 2f
        val centro     = Offset(size.width / 2f, size.height / 2f)
        var anguloInicio = -90f

        items.forEachIndexed { i, (_, pct) ->
            val angulo = pct * 360f / 100f
            drawArc(
                color      = coloresGrafico[i % coloresGrafico.size],
                startAngle = anguloInicio,
                sweepAngle = angulo,
                useCenter  = true,
                topLeft    = Offset(centro.x - radio, centro.y - radio),
                size       = Size(radio * 2, radio * 2)
            )
            anguloInicio += angulo
        }
        // Hueco central (donut)
        drawCircle(color = Color.White, radius = radio * 0.55f, center = centro)
    }
}

// ── Leyenda del pie chart ─────────────────────────────────────────────────────
@Composable
fun PieChartLegenda(
    items: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEachIndexed { i, (nombre, pct) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            coloresGrafico[i % coloresGrafico.size],
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text     = "$nombre ${pct.toInt()}%",
                    fontSize = 11.sp,
                    color    = TextMid
                )
            }
        }
    }
}

// ── Gráfico de barras agrupadas (Distribución) ────────────────────────────────
@Composable
fun BarChartDistribucion(
    items: List<DistribucionItem>,
    modifier: Modifier = Modifier
) {
    val maxVal = items.maxOfOrNull { maxOf(it.pendiente, it.enProceso, it.resuelto) }
        ?.toFloat() ?: 1f

    Canvas(modifier = modifier) {
        val anchoGrupo = size.width / items.size
        val anchoBarra = anchoGrupo * 0.22f
        val gap        = anchoBarra * 0.2f
        val maxAltura  = size.height * 0.85f

        items.forEachIndexed { i, item ->
            val xBase = i * anchoGrupo + anchoGrupo * 0.1f

            listOf(
                item.pendiente to colorPendiente,
                item.enProceso to colorEnProceso,
                item.resuelto  to colorResuelto
            ).forEachIndexed { j, (valor, color) ->
                val altura = if (maxVal > 0) (valor / maxVal) * maxAltura else 0f
                val x      = xBase + j * (anchoBarra + gap)
                drawRect(
                    color   = color,
                    topLeft = Offset(x, size.height - altura),
                    size    = Size(anchoBarra, altura)
                )
            }
        }
    }
}

// ── Gráfico de línea — Tendencia 30 días ─────────────────────────────────────
@Composable
fun LineChartTendencia(
    datos: List<TendenciaDia>,
    modifier: Modifier = Modifier
) {
    if (datos.isEmpty()) return
    val maxVal = datos.maxOfOrNull { it.cantidad }?.toFloat()?.coerceAtLeast(1f) ?: 1f

    Canvas(modifier = modifier) {
        val anchoStep = size.width / (datos.size - 1).coerceAtLeast(1)
        val path      = Path()

        datos.forEachIndexed { i, punto ->
            val x = i * anchoStep
            val y = size.height - (punto.cantidad / maxVal) * size.height * 0.9f
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        // Área bajo la línea
        val pathArea = Path().apply {
            addPath(path)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            path  = pathArea,
            color = SarcGreen.copy(alpha = 0.15f)
        )

        // Línea principal
        drawPath(
            path  = path,
            color = SarcGreen,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Puntos
        datos.forEachIndexed { i, punto ->
            if (punto.cantidad > 0) {
                val x = i * anchoStep
                val y = size.height - (punto.cantidad / maxVal) * size.height * 0.9f
                drawCircle(color = SarcGreen, radius = 3.dp.toPx(), center = Offset(x, y))
            }
        }
    }
}

// ── Eje X con etiquetas de categorías ────────────────────────────────────────
@Composable
fun EjeXCategorias(
    categorias: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        categorias.forEach { cat ->
            Text(
                text     = cat,
                fontSize = 10.sp,
                color    = TextMid,
                maxLines = 1
            )
        }
    }
}

// ── Leyenda horizontal para distribución ─────────────────────────────────────
@Composable
fun LeyendaDistribucion(modifier: Modifier = Modifier) {
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        listOf(
            "Pendiente"  to colorPendiente,
            "En Proceso" to colorEnProceso,
            "Resuelto"   to colorResuelto
        ).forEach { (nombre, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(nombre, fontSize = 10.sp, color = TextMid)
            }
        }
    }
}
