package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.williamsel.sarc.features.administrador.reportesadmin.domain.entities.ReporteAdmin
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.EstadoFiltro
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.ReportesAdminViewModel
import com.williamsel.sarc.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesAdminScreen(
    idUsuario: Int,
    onNavigateBack: () -> Unit,
    viewModel: ReportesAdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredReportes by viewModel.filteredReportes.collectAsState()

    LaunchedEffect(idUsuario) {
        viewModel.cargarReportes(idUsuario)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Todos los Reportes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = SurfaceWhite
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = SurfaceWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SarcGreen
                )
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBarSection(
                query = searchQuery,
                onQueryChange = { viewModel.actualizarBusqueda(it) }
            )

            FilterTabsSection(
                filtroActual = filtroActual,
                onFiltroChange = { viewModel.cambiarFiltro(it) }
            )

            when (val state = uiState) {
                is ReportesAdminUIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SarcGreen)
                    }
                }

                is ReportesAdminUIState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { viewModel.reintentar() }
                    )
                }

                is ReportesAdminUIState.Success -> {
                    // Contador de reportes
                    Text(
                        text = "${filteredReportes.size} reportes encontrados",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 13.sp,
                        color = TextMid,
                        fontWeight = FontWeight.Medium
                    )

                    if (filteredReportes.isEmpty()) {
                        EmptyContent()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = filteredReportes,
                                key = { it.idReporte }
                            ) { reporte ->
                                ReporteCard(reporte = reporte)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Barra de búsqueda
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SearchBarSection(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        placeholder = {
            Text("Buscar reportes...", color = TextLight)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextLight
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceWhite,
            unfocusedContainerColor = SurfaceWhite,
            focusedBorderColor = SarcGreen,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = SarcGreen
        ),
        singleLine = true
    )
}

// ═══════════════════════════════════════════════════════════════
// Filtros (Tabs)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun FilterTabsSection(
    filtroActual: EstadoFiltro,
    onFiltroChange: (EstadoFiltro) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EstadoFiltro.entries.forEach { filtro ->
            val isSelected = filtro == filtroActual

            val bgColor by animateColorAsState(
                targetValue = when {
                    isSelected -> getEstadoColor(filtro)
                    else -> SurfaceWhite
                },
                animationSpec = tween(250),
                label = "filterBg"
            )

            val textColor by animateColorAsState(
                targetValue = when {
                    isSelected -> SurfaceWhite
                    else -> TextMid
                },
                animationSpec = tween(250),
                label = "filterText"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Transparent else FieldBackground,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onFiltroChange(filtro) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = filtro.label,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Tarjeta de reporte
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ReporteCard(reporte: ReporteAdmin) {
    val estadoColor = getEstadoColorById(reporte.idEstado)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // Barra lateral de color + contenido
        Row(modifier = Modifier.fillMaxWidth()) {
            // Indicador lateral de color
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(
                        color = estadoColor,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // ── Título + Badge de estado ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = reporte.titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    EstadoBadge(
                        nombre = reporte.nombreEstado,
                        color = estadoColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── Descripción ──
                Text(
                    text = reporte.descripcion,
                    fontSize = 13.sp,
                    color = TextMid,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ── Info fila 1: Categoría + Usuario ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoChip(
                        icon = Icons.Default.Category,
                        text = reporte.nombreIncidencia,
                        color = TextMid
                    )
                    InfoChip(
                        icon = Icons.Default.Person,
                        text = reporte.nombreUsuario,
                        color = TextMid
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // ── Info fila 2: Ubicación ──
                if (!reporte.ubicacion.isNullOrBlank() && reporte.ubicacion != "Sin ubicación") {
                    InfoChip(
                        icon = Icons.Default.LocationOn,
                        text = reporte.ubicacion,
                        color = TextMid
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // ── Fecha ──
                InfoChip(
                    icon = Icons.Default.CalendarToday,
                    text = formatFecha(reporte.fecha),
                    color = TextLight
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Componentes auxiliares
// ═══════════════════════════════════════════════════════════════

@Composable
private fun EstadoBadge(nombre: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = nombre,
            color = SurfaceWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                color = ErrorRed,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = SarcGreen)
            ) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No se encontraron reportes",
            color = TextLight,
            fontSize = 14.sp
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Utilidades
// ═══════════════════════════════════════════════════════════════

private fun getEstadoColor(filtro: EstadoFiltro): Color {
    return when (filtro) {
        EstadoFiltro.TODOS      -> SarcGreen
        EstadoFiltro.PENDIENTES -> OrangeWarning
        EstadoFiltro.EN_PROCESO -> BlueProceso
        EstadoFiltro.RESUELTOS  -> GreenResuelto
    }
}

private fun getEstadoColorById(idEstado: Int): Color {
    return when (idEstado) {
        1    -> OrangeWarning   // Pendiente
        2    -> BlueProceso     // En Proceso
        3    -> GreenResuelto   // Resuelto
        else -> TextMid
    }
}

private fun formatFecha(fechaStr: String): String {
    return try {
        val dateTime = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "MX"))
        dateTime.format(formatter)
    } catch (e: Exception) {
        fechaStr
    }
}
