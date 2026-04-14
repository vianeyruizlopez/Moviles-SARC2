package com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.williamsel.sarc.features.superadmin.estadisticasglobal.domain.entities.*
import com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.components.*
import com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.viewmodels.EstadisticasGlobalUiState
import com.williamsel.sarc.features.superadmin.estadisticasglobal.presentacion.viewmodels.EstadisticasGlobalViewModel
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.SarcTheme
import com.williamsel.sarc.ui.theme.SurfaceWhite
import com.williamsel.sarc.ui.theme.TextDark
import com.williamsel.sarc.ui.theme.TextMid

// ── Colores de las tarjetas resumen ──────────────────────────────────────────
private val ColorTotalReportes = Color(0xFF1976D2)     // Azul
private val ColorCiudadanos    = Color(0xFF00A878)     // Verde SARC
private val ColorReportes      = Color(0xFFF57C00)     // Naranja
private val ColorAdmins        = Color(0xFF7B1FA2)     // Morado

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun EstadisticasGlobalScreen(
    onBack: () -> Unit = {},
    viewModel: EstadisticasGlobalViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
    ) {
        // ── TopBar ────────────────────────────────────────────────────────────
        EstadisticasTopBar(onBack = onBack)

        // ── Contenido ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F0FF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier         = Modifier.fillMaxWidth().height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SarcGreen)
                    }
                }
                state.errorMessage != null -> {
                    ErrorCard(
                        mensaje   = state.errorMessage!!,
                        onRetry   = viewModel::cargar
                    )
                }
                state.estadisticas != null -> {
                    val stats = state.estadisticas!!
                    DashboardContent(stats = stats)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── TopBar ────────────────────────────────────────────────────────────────────
@Composable
private fun EstadisticasTopBar(onBack: () -> Unit) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = SurfaceWhite)
        }
        Column {
            Text(
                text       = "Estadísticas Globales",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = SurfaceWhite
            )
            Text(
                text     = "Super Administrador",
                fontSize = 12.sp,
                color    = SurfaceWhite.copy(alpha = 0.8f)
            )
        }
    }
}

// ── Contenido principal del dashboard ────────────────────────────────────────
@Composable
private fun DashboardContent(stats: EstadisticasGlobal) {

    // ── 1. Tarjetas resumen ───────────────────────────────────────────────────
    TarjetaResumen(
        label  = "Total Reportes",
        valor  = stats.totalReportes.toString(),
        icon   = Icons.Default.Description,
        color  = ColorTotalReportes
    )
    TarjetaResumen(
        label  = "Ciudadanos",
        valor  = stats.ciudadanos.toString(),
        icon   = Icons.Default.People,
        color  = ColorCiudadanos
    )
    TarjetaResumen(
        label  = "Reportes",
        valor  = stats.reportes.toString(),
        icon   = Icons.AutoMirrored.Filled.TrendingUp,
        color  = ColorReportes
    )
    TarjetaResumen(
        label  = "Admins Activos",
        valor  = stats.adminsActivos.toString(),
        icon   = Icons.Default.AdminPanelSettings,
        color  = ColorAdmins
    )

    // ── 2. Pie chart por categoría ────────────────────────────────────────────
    GraficoCard(titulo = "Reportes por Categoría") {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PieChart(
                items    = stats.reportesPorCategoria.map { it.nombre to it.porcentaje },
                modifier = Modifier.size(130.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                PieChartLegenda(
                    items = stats.reportesPorCategoria.map { it.nombre to it.porcentaje }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text     = "Total de Reportes",
                    fontSize = 11.sp,
                    color    = TextMid
                )
                Text(
                    text       = stats.totalReportes.toString(),
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark
                )
            }
        }
    }

    // ── 3. Pie chart por estado ───────────────────────────────────────────────
    GraficoCard(titulo = "Reportes por Estado") {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PieChart(
                items    = stats.reportesPorEstado.map { it.nombre to it.porcentaje },
                modifier = Modifier.size(130.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                PieChartLegenda(
                    items = stats.reportesPorEstado.map { it.nombre to it.porcentaje }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Total de Reportes", fontSize = 11.sp, color = TextMid)
                Text(
                    text       = stats.totalReportes.toString(),
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark
                )
            }
        }
    }

    // ── 4. Línea de tendencia 30 días ─────────────────────────────────────────
    GraficoCard(titulo = "Tendencia de Reportes Últimos 30 Días") {
        LineChartTendencia(
            datos    = stats.tendencia30Dias,
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total 30 días", fontSize = 11.sp, color = TextMid)
                Text(
                    stats.totalUltimos30Dias.toString(),
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Promedio/día", fontSize = 11.sp, color = TextMid)
                Text(
                    "%.1f".format(stats.promedioUltimos30Dias),
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark
                )
            }
        }
    }

    // ── 5. Distribución por categoría y estado ────────────────────────────────
    GraficoCard(titulo = "Distribución por Categoría y Estado") {
        LeyendaDistribucion(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        BarChartDistribucion(
            items    = stats.distribucion,
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        EjeXCategorias(
            categorias = stats.distribucion.map { it.categoria },
            modifier   = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        // Totales por categoría debajo del gráfico
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            stats.distribucion.forEach { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(item.categoria, fontSize = 11.sp, color = TextMid)
                    Text(
                        (item.pendiente + item.enProceso + item.resuelto).toString(),
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextDark
                    )
                }
            }
        }
    }

    // ── 6. Tiempo promedio de resolución ─────────────────────────────────────
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SarcGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Tiempo Promedio de Resolución", fontSize = 13.sp, color = SurfaceWhite.copy(alpha = 0.85f))
                Text(
                    "${stats.tiempoPromedioHoras}h",
                    fontSize   = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color      = SurfaceWhite
                )
                Text(
                    "${stats.reportesPorEstado.find { it.nombre == "Resuelto" }?.cantidad ?: 0} reportes resueltos",
                    fontSize = 12.sp,
                    color    = SurfaceWhite.copy(alpha = 0.75f)
                )
            }
            Icon(
                Icons.Default.Schedule,
                contentDescription = null,
                tint     = SurfaceWhite.copy(alpha = 0.6f),
                modifier = Modifier.size(48.dp)
            )
        }
    }

    // Tiempos por categoría
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stats.tiemposPorCategoria.forEach { tiempo ->
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(tiempo.emoji, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(tiempo.categoria, fontSize = 13.sp, color = TextDark, fontWeight = FontWeight.Medium)
                    }
                    Text(
                        "${tiempo.horas}h",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint     = Color(0xFFBDBDBD),
                        modifier = Modifier.size(16.dp)
                    )
                }
                HorizontalDivider(color = Color(0xFFF0F0F0))
            }
        }
    }

    // ── 7. Reportes activos ───────────────────────────────────────────────────
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFF1565C0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Reportes Activos", fontSize = 13.sp, color = SurfaceWhite.copy(alpha = 0.85f))
                Text(
                    stats.reportesActivos.toString(),
                    fontSize   = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color      = SurfaceWhite
                )
                Text("Pendientes y en proceso", fontSize = 12.sp, color = SurfaceWhite.copy(alpha = 0.75f))
            }
            Icon(
                Icons.AutoMirrored.Filled.TrendingUp,
                contentDescription = null,
                tint     = SurfaceWhite.copy(alpha = 0.6f),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

// ── Tarjeta resumen con color sólido ─────────────────────────────────────────
@Composable
private fun TarjetaResumen(
    label: String,
    valor: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 13.sp, color = SurfaceWhite.copy(alpha = 0.85f))
                Text(
                    valor,
                    fontSize   = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color      = SurfaceWhite
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfaceWhite.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(24.dp))
            }
        }
    }
}

// ── Card contenedor de gráfico ────────────────────────────────────────────────
@Composable
private fun GraficoCard(
    titulo: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

// ── Card de error ─────────────────────────────────────────────────────────────
@Composable
private fun ErrorCard(mensaje: String, onRetry: () -> Unit) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier            = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(mensaje, fontSize = 14.sp, color = TextMid)
            Button(
                onClick = onRetry,
                colors  = ButtonDefaults.buttonColors(containerColor = SarcGreen)
            ) {
                Text("Reintentar")
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
private val statsFake = EstadisticasGlobal(
    totalReportes = 5,
    ciudadanos    = 3,
    reportes      = 1,
    adminsActivos = 1,
    reportesPorCategoria = listOf(
        CategoriaStats("Bache",     2, 40f),
        CategoriaStats("Alumbrado", 1, 20f),
        CategoriaStats("Basura",    2, 40f)
    ),
    reportesPorEstado = listOf(
        EstadoStats("Pendiente", 3, 60f),
        EstadoStats("En Proceso", 1, 20f),
        EstadoStats("Resuelto", 1, 20f)
    ),
    tendencia30Dias = (1..30).map { TendenciaDia(it, if (it == 15) 3 else 0) },
    totalUltimos30Dias     = 5,
    promedioUltimos30Dias  = 0.2,
    distribucion = listOf(
        DistribucionItem("Bache",     1, 0, 1),
        DistribucionItem("Alumbrado", 1, 0, 0),
        DistribucionItem("Basura",    1, 1, 0),
        DistribucionItem("Otro",      0, 0, 2)
    ),
    tiempoPromedioHoras = 57,
    tiemposPorCategoria = listOf(
        TiempoCategoria("Bache",     "🚧", 0),
        TiempoCategoria("Alumbrado", "💡", 0),
        TiempoCategoria("Basura",    "🗑️", 57)
    ),
    reportesActivos = 4
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    SarcTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SarcGreen)
        ) {
            EstadisticasTopBar(onBack = {})
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F0FF))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardContent(stats = statsFake)
            }
        }
    }
}
