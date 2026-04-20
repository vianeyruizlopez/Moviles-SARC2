package com.williamsel.sarc.features.administrador.mapaadmin.presentacion.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.administrador.mapaadmin.domain.entities.EstadoMapaReporte
import com.williamsel.sarc.features.administrador.mapaadmin.presentacion.viewmodels.MapaAdminViewModel

private val SUCHIAPA = LatLng(16.6167, -93.1000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaAdminScreen(
    onVolver: () -> Unit = {},
    viewModel: MapaAdminViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(SUCHIAPA, 13f)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.verificarPermisoUbicacion()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = state.tienePermisoUbicacion),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = state.tienePermisoUbicacion
            )
        ) {
            state.reportes.forEach { reporte ->
                Marker(
                    state = MarkerState(position = LatLng(reporte.latitud, reporte.longitud)),
                    title = reporte.titulo,
                    snippet = reporte.categoriaLabel,
                    icon = BitmapDescriptorFactory.defaultMarker(reporte.marcadorColor),
                    onClick = {
                        viewModel.onMarcadorSeleccionado(reporte)
                        false
                    }
                )
            }
        }

        // Top Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mapa de Gestión",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Control administrativo territorial",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                    IconButton(onClick = viewModel::cargarReportes) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.mostrarFiltros,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Filtros de Control", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        DropdownSelector(
                            label = "Incidencia",
                            opciones = CategoriaIncidencia.entries.map { it.etiqueta },
                            seleccionado = state.categoriaSeleccionada?.etiqueta ?: "Todos",
                            onSeleccionar = { etiqueta ->
                                val cat = CategoriaIncidencia.fromNombre(etiqueta)
                                viewModel.cambiarCategoria(if (cat == CategoriaIncidencia.TODOS) null else cat)
                            }
                        )

                        DropdownSelector(
                            label = "Estado de Atención",
                            opciones = listOf("Todos") + EstadoMapaReporte.entries.map { it.etiqueta },
                            seleccionado = state.estadoSeleccionado?.etiqueta ?: "Todos",
                            onSeleccionar = { etiqueta ->
                                val est = if (etiqueta == "Todos") null else EstadoMapaReporte.fromNombre(etiqueta)
                                viewModel.cambiarEstado(est)
                            }
                        )
                    }
                }
            }
        }

        if (state.reportes.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 2.dp
            ) {
                Text(
                    text = "${state.reportes.size} reportes activos",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        FloatingActionButton(
            onClick = viewModel::toggleFiltros,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = if (state.mostrarFiltros) Icons.Default.FilterListOff else Icons.Default.FilterList,
                contentDescription = "Filtros"
            )
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    if (state.reporteSeleccionado != null) {
        ModalBottomSheet(
            onDismissRequest = viewModel::onCerrarDetalleReporte,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            DetalleReporteAdminSheet(
                reporte = state.reporteSeleccionado!!,
                onCerrar = viewModel::onCerrarDetalleReporte
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    opciones: List<String>,
    seleccionado: String,
    onSeleccionar: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = seleccionado,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion, fontSize = 13.sp) },
                        onClick = {
                            onSeleccionar(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetalleReporteAdminSheet(
    reporte: ReporteMapaUiModel,
    onCerrar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(2.dp))
                .align(Alignment.CenterHorizontally)
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(shape = RoundedCornerShape(4.dp), color = reporte.categoriaColor, modifier = Modifier.size(12.dp)) {}
            Text(text = reporte.titulo, fontWeight = FontWeight.Bold, fontSize = 17.sp, modifier = Modifier.weight(1f))
            IconButton(onClick = onCerrar, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdminChip(texto = reporte.categoriaLabel)
            AdminChip(texto = reporte.estadoLabel, color = reporte.estadoColor)
        }

        Text(text = reporte.descripcion, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
            Text(text = reporte.coordenadasTexto, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        reporte.fechaTexto?.let { fecha ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                Text(text = fecha, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun AdminChip(texto: String, color: Color = MaterialTheme.colorScheme.secondaryContainer) {
    Surface(shape = RoundedCornerShape(20.dp), color = color) {
        Text(
            text = texto,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = if (color == MaterialTheme.colorScheme.secondaryContainer) MaterialTheme.colorScheme.onSecondaryContainer else Color.White
        )
    }
}
