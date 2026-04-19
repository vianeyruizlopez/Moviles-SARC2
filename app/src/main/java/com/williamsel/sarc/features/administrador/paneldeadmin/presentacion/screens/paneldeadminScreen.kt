package com.williamsel.sarc.features.administrador.paneldeadmin.presentacion.screens

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelDeAdminScreen(
    onNavigateBack: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    viewModel: PanelDeAdminViewModel = hiltViewModel()
) {
    val uiState           by viewModel.uiState.collectAsState()
    val resumen           by viewModel.resumen.collectAsState()
    val vistaActiva       by viewModel.vistaActiva.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val estadoSeleccionado    by viewModel.estadoSeleccionado.collectAsState()
    val searchQuery       by viewModel.searchQuery.collectAsState()
    val filteredReportes  by viewModel.filteredReportes.collectAsState()

    var isSearchExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Panel de Administración", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SurfaceWhite)
                        Text("Gestión de reportes", fontSize = 11.sp, color = SurfaceWhite.copy(alpha = 0.7f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = SurfaceWhite)
                    }
                },
                actions = {
                    IconButton(onClick = { isSearchExpanded = !isSearchExpanded }) {
                        Icon(if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search, null, tint = SurfaceWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SarcGreen)
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            
            // ── Buscador ──
            AnimatedVisibility(visible = isSearchExpanded) {
                Box(modifier = Modifier.fillMaxWidth().background(SarcGreen).padding(horizontal = 16.dp, vertical = 8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.actualizarBusqueda(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("¿Qué reporte buscas?", color = TextLight) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = SurfaceWhite, unfocusedContainerColor = SurfaceWhite),
                        singleLine = true
                    )
                }
            }

            // ── Estadísticas GRANDES ──
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCardLarge(label = "Total", value = resumen.total, color = SarcGreen, modifier = Modifier.weight(1f))
                StatCardLarge(label = "Pend", value = resumen.pendientes, color = OrangeWarning, modifier = Modifier.weight(1f))
                StatCardLarge(label = "Proc", value = resumen.enProceso, color = BlueProceso, modifier = Modifier.weight(1f))
                StatCardLarge(label = "Res", value = resumen.resueltos, color = GreenResuelto, modifier = Modifier.weight(1f))
            }

            // ── Selectores (Vista y Estado) ──
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VistaToggleAnterior(vistaActiva = vistaActiva, onCambiarVista = { viewModel.cambiarVista(it) }, modifier = Modifier.weight(1f))
            }

            // ── Filtros (Categoría y Estado) ──
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(text = "Categoría", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextLight, modifier = Modifier.padding(bottom = 4.dp))
                FilterDropdownAnterior(
                    seleccionado = categoriaSeleccionada,
                    opciones = viewModel.categorias,
                    onSeleccionar = { viewModel.cambiarCategoria(it) }
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Estado", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextLight, modifier = Modifier.padding(bottom = 4.dp))
                FilterDropdownAnterior(
                    seleccionado = estadoSeleccionado,
                    opciones = listOf("Todos", "Pendiente", "En Proceso", "Resuelto"),
                    onSeleccionar = { viewModel.cambiarEstado(it) }
                )
            }

            // ── Lista o Mapa ──
            Box(modifier = Modifier.weight(1f)) {
                when (uiState) {
                    is PanelDeAdminUIState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = SarcGreen) }
                    is PanelDeAdminUIState.Success -> {
                        if (vistaActiva == VistaPanel.LISTA) {
                            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(filteredReportes, key = { it.idReporte }) { reporte ->
                                    ReporteItemV3(
                                        reporte = reporte,
                                        onVerDetalle = { onVerDetalle(reporte.idReporte) },
                                        onCambiarEstado = { id, estado -> viewModel.actualizarEstadoReporte(id, estado) }
                                    )
                                }
                            }
                        } else {
                            MapaReportesContent(reportes = filteredReportes)
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun StatCardLarge(label: String, value: Int, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value.toString(), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = label, fontSize = 12.sp, color = TextLight, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun VistaToggleAnterior(vistaActiva: VistaPanel, onCambiarVista: (VistaPanel) -> Unit, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceWhite)
            .border(1.dp, FieldBackground, RoundedCornerShape(8.dp))
    ) {
        VistaPanel.entries.forEach { vista ->
            val isSelected = vista == vistaActiva
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCambiarVista(vista) }
                    .background(if (isSelected) SarcGreen else Color.Transparent)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (vista == VistaPanel.LISTA) Icons.Default.List else Icons.Default.Map,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (isSelected) Color.White else TextMid
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (vista == VistaPanel.LISTA) "Vista de Lista" else "Vista de Mapa",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else TextMid
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterDropdownAnterior(seleccionado: String, opciones: List<String>, onSeleccionar: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceWhite)
                .border(1.dp, FieldBackground, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(seleccionado, fontSize = 15.sp, color = TextDark)
            Icon(Icons.Default.ArrowDropDown, null, tint = TextLight)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f).background(SurfaceWhite)
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion, fontSize = 15.sp) },
                    onClick = { onSeleccionar(opcion); expanded = false }
                )
            }
        }
    }
}

@Composable
private fun ReporteItemV3(reporte: PanelReporte, onVerDetalle: () -> Unit, onCambiarEstado: (Int, Int) -> Unit) {
    val color = getEstadoColorById(reporte.idEstado)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onVerDetalle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(reporte.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${reporte.nombreIncidencia} • ${reporte.nombreUsuario}", fontSize = 13.sp, color = TextLight)
            }
            Row {
                IconButton(onClick = onVerDetalle) {
                    Icon(Icons.Default.Visibility, "Ver", tint = SarcGreen)
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.EditCalendar, "Estado", tint = BlueProceso)
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text("Pendiente") }, onClick = { onCambiarEstado(reporte.idReporte, 1); showMenu = false })
                        DropdownMenuItem(text = { Text("En Proceso") }, onClick = { onCambiarEstado(reporte.idReporte, 2); showMenu = false })
                        DropdownMenuItem(text = { Text("Resuelto") }, onClick = { onCambiarEstado(reporte.idReporte, 3); showMenu = false })
                    }
                }
            }
        }
    }
}

@Composable
private fun MapaReportesContent(reportes: List<PanelReporte>) {
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(16.6069, -93.1027), 12f) }
    GoogleMap(modifier = Modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(20.dp)), cameraPositionState = cameraPositionState) {
        reportes.forEach { rep ->
            rep.latitud?.let { lat -> rep.longitud?.let { lng ->
                Marker(state = MarkerState(LatLng(lat, lng)), title = rep.titulo, icon = BitmapDescriptorFactory.defaultMarker(when(rep.idEstado){1->40f; 2->210f; else->120f}))
            }}
        }
    }
}

private fun getEstadoColorById(id: Int) = when(id){ 1->OrangeWarning; 2->BlueProceso; 3->GreenResuelto; else->TextMid }
