package com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.screens

import android.Manifest
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroCategoriaDto
import com.williamsel.sarc.features.ciudadano.mapaciu.data.models.FiltroEstadoDto
import com.williamsel.sarc.features.ciudadano.mapaciu.domain.entities.ReporteMapa
import com.williamsel.sarc.features.ciudadano.mapaciu.presentacion.viewmodels.MapaCiudadanoViewModel
import java.text.SimpleDateFormat
import java.util.*

private val SUCHIAPA = LatLng(16.6167, -93.1000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaCiudadanoScreen(
    onBack: () -> Unit = {},
    viewModel: MapaCiudadanoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(SUCHIAPA, 12f)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = androidx.compose.ui.platform.LocalContext.current
    val hasLocationPermission = remember {
        androidx.core.content.ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        androidx.core.content.ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {
            state.reportes.forEach { reporte ->
                val posicion = LatLng(reporte.latitud, reporte.longitud)
                Marker(
                    state = MarkerState(position = posicion),
                    title = reporte.nombre,
                    snippet = categoriaLabel(reporte.idIncidencia),
                    icon = BitmapDescriptorFactory.defaultMarker(
                        colorPorCategoria(reporte.idIncidencia)
                    ),
                    onClick = {
                        viewModel.onMarcadorSeleccionado(reporte)
                        false
                    }
                )
            }
        }

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
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mapa Interactivo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Visualización geográfica",
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
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Filtros",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            text = "Categoría",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                        DropdownFiltro(
                            opciones = FiltroCategoriaDto.entries.map { it.label },
                            seleccionado = state.filtroCategoria.label,
                            onSeleccionar = { label ->
                                val filtro = FiltroCategoriaDto.entries.first { it.label == label }
                                viewModel.onFiltroCategoriaChanged(filtro)
                            }
                        )

                        Text(
                            text = "Estado",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                        DropdownFiltro(
                            opciones = FiltroEstadoDto.entries.map { it.label },
                            seleccionado = state.filtroEstado.label,
                            onSeleccionar = { label ->
                                val filtro = FiltroEstadoDto.entries.first { it.label == label }
                                viewModel.onFiltroEstadoChanged(filtro)
                            }
                        )
                    }
                }
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
                imageVector = if (state.mostrarFiltros) Icons.Default.FilterListOff
                              else Icons.Default.FilterList,
                contentDescription = "Filtros"
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                    text = "${state.reportes.size} reportes",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }

    if (state.reporteSeleccionado != null) {
        ModalBottomSheet(
            onDismissRequest = viewModel::onCerrarDetalleReporte,
            sheetState = sheetState
        ) {
            DetalleReporteSheet(
                reporte = state.reporteSeleccionado!!,
                onCerrar = viewModel::onCerrarDetalleReporte
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownFiltro(
    opciones: List<String>,
    seleccionado: String,
    onSeleccionar: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = it }
    ) {
        OutlinedTextField(
            value = seleccionado,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion, fontSize = 13.sp) },
                    onClick = {
                        onSeleccionar(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DetalleReporteSheet(
    reporte: ReporteMapa,
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
                .background(
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(2.dp)
                )
                .align(Alignment.CenterHorizontally)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = colorComposePorCategoria(reporte.idIncidencia),
                modifier = Modifier.size(12.dp)
            ) {}
            Text(
                text = reporte.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onCerrar, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip(texto = categoriaLabel(reporte.idIncidencia))
            Chip(
                texto = estadoLabel(reporte.idEstado),
                color = colorEstado(reporte.idEstado)
            )
        }

        Text(
            text = reporte.descripcion,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "%.6f, %.6f".format(reporte.latitud, reporte.longitud),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        reporte.fechaReporte?.let { ts ->
            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "MX"))
                .format(Date(ts))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = fecha,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun Chip(texto: String, color: Color = MaterialTheme.colorScheme.secondaryContainer) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color
    ) {
        Text(
            text = texto,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}


private fun colorPorCategoria(idIncidencia: Int?): Float = when (idIncidencia) {
    1 -> BitmapDescriptorFactory.HUE_RED
    2 -> BitmapDescriptorFactory.HUE_YELLOW
    3 -> BitmapDescriptorFactory.HUE_ORANGE
    4 -> BitmapDescriptorFactory.HUE_BLUE
    else -> BitmapDescriptorFactory.HUE_VIOLET
}

@Composable
private fun colorComposePorCategoria(idIncidencia: Int?): Color = when (idIncidencia) {
    1 -> Color(0xFFE53935)
    2 -> Color(0xFFFDD835)
    3 -> Color(0xFFFF8F00)
    4 -> Color(0xFF1E88E5)
    else -> MaterialTheme.colorScheme.secondary
}

private fun categoriaLabel(idIncidencia: Int?): String = when (idIncidencia) {
    1 -> "Bache"
    2 -> "Basura"
    3 -> "Alumbrado"
    4 -> "Otro"
    else -> "Sin categoría"
}

@Composable
private fun colorEstado(idEstado: Int?): Color = when (idEstado) {
    1 -> Color(0xFFFFECB3)
    2 -> Color(0xFFBBDEFB)
    3 -> Color(0xFFC8E6C9)
    else -> MaterialTheme.colorScheme.surfaceVariant
}

private fun estadoLabel(idEstado: Int?): String = when (idEstado) {
    1 -> "Pendiente"
    2 -> "En proceso"
    3 -> "Resuelto"
    else -> "Sin estado"
}
