package com.williamsel.sarc.features.ciudadano.misreportesciu.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.williamsel.sarc.features.ciudadano.misreportesciu.domain.entities.ReporteCiudadano
import com.williamsel.sarc.features.ciudadano.misreportesciu.presentacion.viewmodels.MisReportesViewModel
import com.williamsel.sarc.ui.theme.BlueProceso
import com.williamsel.sarc.ui.theme.GreenResuelto
import com.williamsel.sarc.ui.theme.OrangeWarning
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.SarcTheme
import com.williamsel.sarc.ui.theme.SurfaceWhite
import com.williamsel.sarc.ui.theme.TextDark
import com.williamsel.sarc.ui.theme.TextLight
import com.williamsel.sarc.ui.theme.TextMid
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Composable
fun MisReportesScreen(
    idUsuario: Int = 1,
    onBack: () -> Unit = {},
    onEditarReporte: (Int) -> Unit = {},
    viewModel: MisReportesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(idUsuario) { viewModel.cargarReportes(idUsuario) }

    state.errorMessage?.let { LaunchedEffect(it) { viewModel.clearError() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
    ) {
        MisReportesTopBar(onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = SurfaceWhite,
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
                )
        ) {
            OutlinedTextField(
                value         = state.busqueda,
                onValueChange = { viewModel.onBusquedaChange(idUsuario, it) },
                placeholder   = { Text("Buscar reportes...", color = TextLight) },
                leadingIcon   = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = TextLight
                    )
                },
                singleLine = true,
                shape      = RoundedCornerShape(12.dp),
                modifier   = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = SarcGreen,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedTextColor     = TextDark,
                    unfocusedTextColor   = TextDark,
                    cursorColor          = SarcGreen
                )
            )

            FiltroRow(
                filtroActivo = state.filtroActivo,
                onFiltroChange = { viewModel.onFiltroChange(idUsuario, it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text     = "${state.reportesFiltrados.size} " +
                           if (state.reportesFiltrados.size == 1) "reporte encontrado"
                           else "reportes encontrados",
                fontSize = 13.sp,
                color    = TextMid,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SarcGreen)
                }
            } else if (state.reportesFiltrados.isEmpty()) {
                EstadoVacio()
            } else {
                LazyColumn(
                    contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement   = Arrangement.spacedBy(12.dp),
                    modifier              = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.reportesFiltrados,
                        key   = { it.idReporte ?: 0 }
                    ) { reporte ->
                        ReporteCard(
                            reporte          = reporte,
                            onEditarReporte  = onEditarReporte
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MisReportesTopBar(onBack: () -> Unit) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint               = SurfaceWhite
            )
        }
        Text(
            text       = "Mis Reportes",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = SurfaceWhite
        )
    }
}

@Composable
private fun FiltroRow(
    filtroActivo: FiltroEstado,
    onFiltroChange: (FiltroEstado) -> Unit
) {
    Row(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FiltroEstado.entries.forEach { filtro ->
            val activo = filtro == filtroActivo
            val colorFondo = when {
                !activo -> Color(0xFFF0F0F0)
                filtro == FiltroEstado.PENDIENTE  -> OrangeWarning
                filtro == FiltroEstado.EN_PROCESO -> BlueProceso
                filtro == FiltroEstado.RESUELTO   -> GreenResuelto
                else                              -> SarcGreen
            }
            val colorTexto = if (activo) SurfaceWhite else TextMid

            FilterChip(
                selected = activo,
                onClick  = { onFiltroChange(filtro) },
                label    = {
                    Text(
                        text     = filtro.label,
                        fontSize = 12.sp,
                        color    = colorTexto
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor   = colorFondo,
                    containerColor           = Color(0xFFF0F0F0),
                    selectedLabelColor       = SurfaceWhite,
                    labelColor               = TextMid
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled        = true,
                    selected       = activo,
                    borderColor    = Color.Transparent,
                    selectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun ReporteCard(
    reporte: ReporteCiudadano,
    onEditarReporte: (Int) -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                Text(
                    text       = reporte.titulo,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextDark,
                    modifier   = Modifier.weight(1f).padding(end = 8.dp)
                )
                EstadoBadge(estado = reporte.estado, idEstado = reporte.idEstado)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text     = reporte.descripcion,
                fontSize = 12.sp,
                color    = TextMid,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetaItem(
                    icon  = Icons.Default.Category,
                    texto = reporte.incidencia,
                    modifier = Modifier.weight(1f)
                )
                MetaItem(
                    icon  = Icons.Default.LocationOn,
                    texto = reporte.ubicacion,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint               = TextLight,
                    modifier           = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text     = formatearFecha(reporte.fechaReporte),
                    fontSize = 11.sp,
                    color    = TextLight
                )
            }

            if (reporte.puedeEditar) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick          = { reporte.idReporte?.let { onEditarReporte(it) } },
                    contentPadding   = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Edit,
                        contentDescription = null,
                        tint               = SarcGreen,
                        modifier           = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text     = "Puedes editar este reporte",
                        fontSize = 12.sp,
                        color    = SarcGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Composable
private fun EstadoBadge(estado: String, idEstado: Int) {
    val color = when (idEstado) {
        1    -> OrangeWarning
        2    -> BlueProceso
        3    -> GreenResuelto
        else -> TextMid
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color
    ) {
        Text(
            text     = estado,
            fontSize = 10.sp,
            color    = SurfaceWhite,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun MetaItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier          = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = TextLight,
            modifier           = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text     = texto,
            fontSize = 11.sp,
            color    = TextMid,
            maxLines = 1
        )
    }
}

@Composable
private fun EstadoVacio() {
    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector        = Icons.Default.SearchOff,
                contentDescription = null,
                tint               = Color(0xFFBDBDBD),
                modifier           = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text       = "No se encontraron reportes",
                fontSize   = 16.sp,
                fontWeight = FontWeight.Medium,
                color      = TextMid
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text     = "Aún no hay reportes registrados",
                fontSize = 13.sp,
                color    = TextLight
            )
        }
    }
}

private fun formatearFecha(timestamp: Long): String {
    if (timestamp == 0L) return "Fecha desconocida"
    return try {
        val sdf = SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "MX"))
        sdf.format(Date(timestamp))
    } catch (e: Exception) {
        "Fecha desconocida"
    }
}
