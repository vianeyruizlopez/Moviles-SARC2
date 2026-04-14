package com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.williamsel.sarc.features.administrador.paneldeadmin.domain.entities.PanelReporte
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.viewmodels.PanelDeAdminViewModel
import com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.viewmodels.VistaPanel
import com.williamsel.sarc.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// ═══════════════════════════════════════════════════════════════
// Pantalla principal
// ═══════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelDeAdminScreen(
    onNavigateBack: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    viewModel: PanelDeAdminViewModel = hiltViewModel()
) {
    val uiState           by viewModel.uiState.collectAsState()
    val vistaActiva       by viewModel.vistaActiva.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val estadoSeleccionado    by viewModel.estadoSeleccionado.collectAsState()
    val searchQuery       by viewModel.searchQuery.collectAsState()
    val filteredReportes  by viewModel.filteredReportes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Panel de Administración",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = SurfaceWhite
                        )
                        Text(
                            text = "Gestión de reportes ciudadanos",
                            fontSize = 12.sp,
                            color = SurfaceWhite.copy(alpha = 0.8f)
                        )
                    }
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
        when (uiState) {
            is PanelDeAdminUIState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SarcGreen)
                }
            }

            is PanelDeAdminUIState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (uiState as PanelDeAdminUIState.Error).message,
                            color = ErrorRed,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.cargarReportes() },
                            colors = ButtonDefaults.buttonColors(containerColor = SarcGreen)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is PanelDeAdminUIState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // ── Sección fija: Stats + Toggle + Filtros ──
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .weight(0f, fill = false)
                    ) {
                        // Tarjetas de estadísticas
                        EstadisticasGrid(
                            total = viewModel.totalReportes,
                            pendientes = viewModel.pendientes,
                            enProceso = viewModel.enProceso,
                            resueltos = viewModel.resueltos
                        )

                        // Toggle Vista de Lista / Vista de Mapa
                        VistaToggle(
                            vistaActiva = vistaActiva,
                            onCambiarVista = { viewModel.cambiarVista(it) }
                        )

                        // Filtros (búsqueda, categoría, estado)
                        FiltrosSection(
                            searchQuery = searchQuery,
                            onSearchChange = { viewModel.actualizarBusqueda(it) },
                            categoriaSeleccionada = categoriaSeleccionada,
                            categorias = viewModel.categorias,
                            onCategoriaChange = { viewModel.cambiarCategoria(it) },
                            estadoSeleccionado = estadoSeleccionado,
                            onEstadoChange = { viewModel.cambiarEstado(it) }
                        )

                        // Contador
                        Text(
                            text = "Mostrando ${filteredReportes.size} de ${viewModel.totalReportes} reportes",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            color = TextLight
                        )
                    }

                    // ── Contenido según vista ──
                    when (vistaActiva) {
                        VistaPanel.LISTA -> {
                            ListaReportesContent(
                                reportes = filteredReportes,
                                onVerDetalle = onVerDetalle,
                                onActualizarEstado = { id, estado ->
                                    viewModel.actualizarEstadoReporte(id, estado)
                                },
                                onEliminar = { viewModel.eliminarReporte(it) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        VistaPanel.MAPA -> {
                            MapaReportesContent(
                                reportes = filteredReportes,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Grid de estadísticas (2x2)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun EstadisticasGrid(
    total: Int,
    pendientes: Int,
    enProceso: Int,
    resueltos: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                label = "Total",
                value = total.toString(),
                color = SarcGreen,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Pendientes",
                value = pendientes.toString(),
                color = OrangeWarning,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                label = "En Proceso",
                value = enProceso.toString(),
                color = BlueProceso,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Resueltos",
                value = resueltos.toString(),
                color = GreenResuelto,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextLight,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Toggle Vista de Lista / Vista de Mapa
// ═══════════════════════════════════════════════════════════════

@Composable
private fun VistaToggle(
    vistaActiva: VistaPanel,
    onCambiarVista: (VistaPanel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, FieldBackground, RoundedCornerShape(12.dp))
            .background(SurfaceWhite)
    ) {
        VistaPanel.entries.forEach { vista ->
            val isSelected = vista == vistaActiva

            val bgColor by animateColorAsState(
                targetValue = if (isSelected) SarcGreen else SurfaceWhite,
                animationSpec = tween(250),
                label = "toggleBg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) SurfaceWhite else TextMid,
                animationSpec = tween(250),
                label = "toggleText"
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .clickable { onCambiarVista(vista) }
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (vista == VistaPanel.LISTA)
                        Icons.AutoMirrored.Filled.List else Icons.Default.Map,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (vista == VistaPanel.LISTA) "Vista de Lista" else "Vista de Mapa",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Sección de filtros
// ═══════════════════════════════════════════════════════════════

@Composable
private fun FiltrosSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    categoriaSeleccionada: String,
    categorias: List<String>,
    onCategoriaChange: (String) -> Unit,
    estadoSeleccionado: String,
    onEstadoChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por título, descripción o ciud", color = TextLight, fontSize = 14.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = TextLight)
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,
                focusedBorderColor = SarcGreen,
                unfocusedBorderColor = FieldBackground,
                cursorColor = SarcGreen
            ),
            singleLine = true
        )

        // Dropdown de Categoría
        FiltroDropdown(
            label = "Categoría",
            valorActual = categoriaSeleccionada,
            opciones = categorias,
            onSeleccionar = onCategoriaChange
        )

        // Dropdown de Estado
        FiltroDropdown(
            label = "Estado",
            valorActual = estadoSeleccionado,
            opciones = listOf("Todos", "Pendientes", "En Proceso", "Resueltos"),
            onSeleccionar = onEstadoChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltroDropdown(
    label: String,
    valorActual: String,
    opciones: List<String>,
    onSeleccionar: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextMid,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))

        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = valorActual,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite,
                    focusedBorderColor = SarcGreen,
                    unfocusedBorderColor = FieldBackground
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion, fontSize = 14.sp) },
                        onClick = {
                            onSeleccionar(opcion)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Vista de Lista
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ListaReportesContent(
    reportes: List<PanelReporte>,
    onVerDetalle: (Int) -> Unit,
    onActualizarEstado: (Int, Int) -> Unit,
    onEliminar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (reportes.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("No se encontraron reportes", color = TextLight, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = reportes, key = { it.idReporte }) { reporte ->
                ReporteAdminCard(
                    reporte = reporte,
                    onVerDetalle = { onVerDetalle(reporte.idReporte) },
                    onActualizarEstado = onActualizarEstado,
                    onEliminar = { onEliminar(reporte.idReporte) }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Tarjeta de reporte con acciones de admin
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ReporteAdminCard(
    reporte: PanelReporte,
    onVerDetalle: () -> Unit,
    onActualizarEstado: (Int, Int) -> Unit,
    onEliminar: () -> Unit
) {
    val estadoColor = getEstadoColorById(reporte.idEstado)

    // Diálogo para confirmar eliminación
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Diálogo para cambiar estado
    var showEstadoDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Reporte", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar este reporte? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar()
                    showDeleteDialog = false
                }) {
                    Text("Eliminar", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = TextMid)
                }
            }
        )
    }

    if (showEstadoDialog) {
        EstadoSelectorDialog(
            currentEstado = reporte.idEstado,
            onEstadoSelected = { nuevoEstado ->
                onActualizarEstado(reporte.idReporte, nuevoEstado)
                showEstadoDialog = false
            },
            onDismiss = { showEstadoDialog = false }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // ── Título + Badge + Iconos de acción ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reporte.titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextDark,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Badge de estado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(estadoColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = reporte.nombreEstado,
                        color = SurfaceWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Botón ver detalle
                IconButton(
                    onClick = onVerDetalle,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = "Ver detalle",
                        tint = SarcGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Botón eliminar
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = ErrorRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ── Descripción ──
            Text(
                text = reporte.descripcion,
                fontSize = 13.sp,
                color = TextMid,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── Info: Categoría + Usuario ──
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                InfoChip(Icons.Default.Category, reporte.nombreIncidencia)
                InfoChip(Icons.Default.Person, reporte.nombreUsuario)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ── Ubicación ──
            if (!reporte.ubicacion.isNullOrBlank()) {
                InfoChip(Icons.Default.LocationOn, reporte.ubicacion)
                Spacer(modifier = Modifier.height(4.dp))
            }

            // ── Fecha ──
            InfoChip(Icons.Default.CalendarToday, formatFecha(reporte.fecha))

            Spacer(modifier = Modifier.height(12.dp))

            // ── Botones: Estado actual + Actualizar Estado ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chip de estado actual
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(estadoColor)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = reporte.nombreEstado,
                        color = SurfaceWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón Actualizar Estado
                OutlinedButton(
                    onClick = { showEstadoDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextDark
                    ),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text(
                        text = "Actualizar Estado",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Diálogo selector de estado
// ═══════════════════════════════════════════════════════════════

@Composable
private fun EstadoSelectorDialog(
    currentEstado: Int,
    onEstadoSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val estados = listOf(
        Triple(1, "Pendiente", OrangeWarning),
        Triple(2, "En Proceso", BlueProceso),
        Triple(3, "Resuelto", GreenResuelto)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Cambiar Estado", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                estados.forEach { (id, nombre, color) ->
                    val isCurrentState = id == currentEstado

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isCurrentState) color.copy(alpha = 0.1f) else Color.Transparent)
                            .border(
                                width = if (isCurrentState) 2.dp else 1.dp,
                                color = if (isCurrentState) color else FieldBackground,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable(enabled = !isCurrentState) { onEstadoSelected(id) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        Text(
                            text = nombre,
                            fontSize = 14.sp,
                            fontWeight = if (isCurrentState) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCurrentState) color else TextDark,
                            modifier = Modifier.weight(1f)
                        )
                        if (isCurrentState) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextMid)
            }
        }
    )
}

// ═══════════════════════════════════════════════════════════════
// Vista de Mapa (Google Maps embebido)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun MapaReportesContent(
    reportes: List<PanelReporte>,
    modifier: Modifier = Modifier
) {
    // Centrar en Suchiapa/Tuxtla
    val posicionInicial = LatLng(16.6069, -93.1027)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicionInicial, 11f)
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false
        ),
        properties = MapProperties(mapType = MapType.NORMAL)
    ) {
        reportes.forEach { reporte ->
            val lat = reporte.latitud ?: return@forEach
            val lng = reporte.longitud ?: return@forEach
            if (lat == 0.0 && lng == 0.0) return@forEach

            val pos = LatLng(lat, lng)
            val hue = when (reporte.idEstado) {
                1    -> BitmapDescriptorFactory.HUE_ORANGE
                2    -> BitmapDescriptorFactory.HUE_AZURE
                3    -> BitmapDescriptorFactory.HUE_GREEN
                else -> BitmapDescriptorFactory.HUE_RED
            }

            Marker(
                state = MarkerState(position = pos),
                title = reporte.titulo,
                snippet = "${reporte.nombreEstado} · ${reporte.nombreIncidencia}",
                icon = BitmapDescriptorFactory.defaultMarker(hue)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Componentes auxiliares
// ═══════════════════════════════════════════════════════════════

@Composable
private fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextLight,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = TextLight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun getEstadoColorById(idEstado: Int): Color {
    return when (idEstado) {
        1    -> OrangeWarning
        2    -> BlueProceso
        3    -> GreenResuelto
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
