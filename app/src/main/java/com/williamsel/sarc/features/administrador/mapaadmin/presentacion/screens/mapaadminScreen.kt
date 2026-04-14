package com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.MapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.presentacion.viewmodels.MapaAdminViewModel
import com.williamsel.sarc.ui.theme.*
import kotlinx.coroutines.launch

// ── Colores de marcador por estado ────────────────────────────────────────────
private fun hueParaEstado(estado: EstadoMapaReporte): Float = when (estado) {
    EstadoMapaReporte.PENDIENTE  -> BitmapDescriptorFactory.HUE_ORANGE
    EstadoMapaReporte.EN_PROCESO -> BitmapDescriptorFactory.HUE_AZURE
    EstadoMapaReporte.RESUELTO   -> BitmapDescriptorFactory.HUE_GREEN
}

private val ColorPendiente  = Color(0xFFFF9800)
private val ColorEnProceso  = Color(0xFF2196F3)
private val ColorResuelto   = Color(0xFF4CAF50)

private fun colorEstado(estado: EstadoMapaReporte) = when (estado) {
    EstadoMapaReporte.PENDIENTE  -> ColorPendiente
    EstadoMapaReporte.EN_PROCESO -> ColorEnProceso
    EstadoMapaReporte.RESUELTO   -> ColorResuelto
}

// ── Pantalla principal ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaAdminScreen(
    onVolver: () -> Unit = {},
    viewModel: MapaAdminViewModel = hiltViewModel()
) {
    val uiState           by viewModel.uiState.collectAsStateWithLifecycle()
    val categoriaActiva   by viewModel.categoriaSeleccionada.collectAsStateWithLifecycle()
    val estadoActivo      by viewModel.estadoSeleccionado.collectAsStateWithLifecycle()

    // Posición inicial: Suchiapa / Tuxtla área
    val posicionInicial = LatLng(16.6069, -93.1027)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicionInicial, 11f)
    }

    // Bottom sheet de detalle
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope      = rememberCoroutineScope()
    var reporteSeleccionado by remember { mutableStateOf<MapaReporte?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Google Map ───────────────────────────────────────
        GoogleMap(
            modifier            = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings          = MapUiSettings(
                zoomControlsEnabled   = false,
                myLocationButtonEnabled = false,
                mapToolbarEnabled     = false
            ),
            properties = MapProperties(mapType = MapType.NORMAL)
        ) {
            if (uiState is MapaAdminUIState.Success) {
                val reportes = (uiState as MapaAdminUIState.Success).reportes
                reportes.forEach { reporte ->
                    val pos = LatLng(reporte.latitud, reporte.longitud)
                    Marker(
                        state   = MarkerState(position = pos),
                        title   = reporte.titulo,
                        snippet = reporte.estado.etiqueta,
                        icon    = BitmapDescriptorFactory.defaultMarker(hueParaEstado(reporte.estado)),
                        onClick = { _ ->
                            reporteSeleccionado = reporte
                            scope.launch { sheetState.show() }
                            true
                        }
                    )
                }
            }
        }

        // ── Top bar flotante ──────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Cabecera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(SarcGreen)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick  = onVolver,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mapa Interactivo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Visualización geográfica", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                if (uiState is MapaAdminUIState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                }
            }

            // Panel de filtros
            FiltrosPanel(
                categoriaActiva = categoriaActiva,
                estadoActivo    = estadoActivo,
                onCategoriaChange = viewModel::cambiarCategoria,
                onEstadoChange    = viewModel::cambiarEstado
            )
        }

        // ── Leyenda de colores (abajo derecha) ───────────────
        LeyendaEstados(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 24.dp)
        )

        // ── Error overlay ─────────────────────────────────────
        if (uiState is MapaAdminUIState.Error) {
            Surface(
                modifier  = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape     = RoundedCornerShape(12.dp),
                color     = Color(0xFFFCE4EC),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier              = Modifier.padding(12.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFD32F2F))
                    Text(
                        (uiState as MapaAdminUIState.Error).mensaje,
                        color    = Color(0xFFD32F2F),
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = viewModel::cargarReportes) {
                        Text("Reintentar", color = SarcGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // ── Bottom Sheet de detalle del reporte ───────────────────
    reporteSeleccionado?.let { reporte ->
        if (sheetState.isVisible || sheetState.currentValue != SheetValue.Hidden) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }
                    reporteSeleccionado = null
                },
                sheetState     = sheetState,
                shape          = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                containerColor = BackgroundLight
            ) {
                DetalleReporteSheet(reporte = reporte)
            }
        }
    }
}

// ── Panel de filtros ──────────────────────────────────────────────────────────

@Composable
private fun FiltrosPanel(
    categoriaActiva: CategoriaIncidencia?,
    estadoActivo: EstadoMapaReporte?,
    onCategoriaChange: (CategoriaIncidencia?) -> Unit,
    onEstadoChange: (EstadoMapaReporte?) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceWhite)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Cabecera del panel
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .clickable { expandido = !expandido },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.FilterList, contentDescription = null, tint = SarcGreen, modifier = Modifier.size(18.dp))
            Text("Filtros", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextDark, modifier = Modifier.weight(1f))
            Icon(
                if (expandido) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null, tint = TextLight
            )
        }

        if (expandido) {
            // Categoría
            Text("Categoría", fontSize = 11.sp, color = TextLight, fontWeight = FontWeight.Medium)
            FiltroDropdown(
                label    = categoriaActiva?.etiqueta ?: "Todos",
                opciones = listOf("Todos") + CategoriaIncidencia.entries
                    .filter { it != CategoriaIncidencia.TODOS }
                    .map { it.etiqueta },
                onSeleccionar = { seleccion ->
                    onCategoriaChange(
                        if (seleccion == "Todos") null
                        else CategoriaIncidencia.entries.find { it.etiqueta == seleccion }
                    )
                }
            )

            // Estado
            Text("Estado", fontSize = 11.sp, color = TextLight, fontWeight = FontWeight.Medium)
            FiltroDropdown(
                label    = estadoActivo?.etiqueta ?: "Todos",
                opciones = listOf("Todos") + EstadoMapaReporte.entries.map { it.etiqueta },
                onSeleccionar = { seleccion ->
                    onEstadoChange(
                        if (seleccion == "Todos") null
                        else EstadoMapaReporte.entries.find { it.etiqueta == seleccion }
                    )
                }
            )
        } else {
            // Resumen compacto de filtros activos
            val resumen = buildString {
                append(categoriaActiva?.etiqueta ?: "Todos")
                append(" · ")
                append(estadoActivo?.etiqueta ?: "Todos")
            }
            Text(resumen, fontSize = 12.sp, color = TextLight)
        }
    }
}

@Composable
private fun FiltroDropdown(
    label: String,
    opciones: List<String>,
    onSeleccionar: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick       = { expandido = true },
            shape         = RoundedCornerShape(8.dp),
            colors        = ButtonDefaults.outlinedButtonColors(contentColor = TextDark),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier      = Modifier.fillMaxWidth()
        ) {
            Text(label, fontSize = 13.sp, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(18.dp))
        }
        DropdownMenu(
            expanded         = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text    = { Text(opcion, fontSize = 13.sp) },
                    onClick = {
                        onSeleccionar(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}

// ── Leyenda ───────────────────────────────────────────────────────────────────

@Composable
private fun LeyendaEstados(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceWhite)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LeyendaItem(color = ColorPendiente,  label = "Pendiente")
        LeyendaItem(color = ColorEnProceso,  label = "En Proceso")
        LeyendaItem(color = ColorResuelto,   label = "Resuelto")
    }
}

@Composable
private fun LeyendaItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Text(label, fontSize = 11.sp, color = TextDark)
    }
}

// ── Bottom Sheet de detalle ───────────────────────────────────────────────────

@Composable
private fun DetalleReporteSheet(reporte: MapaReporte) {
    val estadoColor = colorEstado(reporte.estado)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Indicador
        Box(
            modifier = Modifier
                .width(40.dp).height(4.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(4.dp))

        // Estado badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(reporte.titulo, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextDark, modifier = Modifier.weight(1f))
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = estadoColor.copy(alpha = 0.15f)
            ) {
                Text(
                    reporte.estado.etiqueta,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = estadoColor,
                    modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Text(reporte.descripcion, fontSize = 14.sp, color = TextLight)

        HorizontalDivider(color = Color(0xFFF0F0F0))

        DetalleRow(icon = Icons.Default.Category,   label = "Incidencia", valor = reporte.incidencia.etiqueta)
        DetalleRow(icon = Icons.Default.Person,      label = "Reportado por", valor = reporte.nombreUsuario)
        DetalleRow(icon = Icons.Default.LocationOn,  label = "Ubicación", valor = reporte.ubicacion)
        DetalleRow(icon = Icons.Default.CalendarToday, label = "Fecha",    valor = reporte.fecha.take(10))
        DetalleRow(
            icon  = Icons.Default.Map,
            label = "Coordenadas",
            valor = "%.5f, %.5f".format(reporte.latitud, reporte.longitud)
        )
    }
}

@Composable
private fun DetalleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    valor: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = SarcGreen, modifier = Modifier.size(18.dp).padding(top = 1.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextLight)
            Text(valor, fontSize = 13.sp, color = TextDark, fontWeight = FontWeight.Medium)
        }
    }
}
