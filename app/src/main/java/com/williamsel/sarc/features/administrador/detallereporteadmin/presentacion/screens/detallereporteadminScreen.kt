package com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.viewmodels.DetalleReporteAdminViewModel
import com.williamsel.sarc.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleReporteAdminScreen(
    idReporte: Int,
    onNavigateBack: () -> Unit,
    onComoLlegar: (Double, Double) -> Unit,
    viewModel: DetalleReporteAdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(idReporte) {
        viewModel.cargarDetalle(idReporte)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detalle del Reporte", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = SurfaceWhite)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = SurfaceWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SarcGreen)
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        when (val state = uiState) {
            is DetalleReporteAdminUIState.Loading -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SarcGreen)
                }
            }
            is DetalleReporteAdminUIState.Error -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = ErrorRed, fontSize = 14.sp)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.reintentar() }, colors = ButtonDefaults.buttonColors(containerColor = SarcGreen)) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            is DetalleReporteAdminUIState.Success -> {
                DetalleContent(
                    reporte = state.reporte,
                    paddingValues = paddingValues,
                    onComoLlegar = onComoLlegar
                )
            }
        }
    }
}

@Composable
private fun DetalleContent(
    reporte: DetalleReporteUIModel, 
    paddingValues: PaddingValues,
    onComoLlegar: (Double, Double) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            
            ImagenConEstado(
                imagenUrl = reporte.imagen,
                nombreEstado = reporte.nombreEstado,
                estadoColor = reporte.estadoColor 
            )

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(16.dp))

                CategoriaSection(
                    nombreIncidencia = reporte.nombreIncidencia,
                    emoji = reporte.categoriaEmoji 
                )

                Spacer(Modifier.height(16.dp))

                Text(text = reporte.titulo, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextDark)

                Spacer(Modifier.height(20.dp))

                InfoSection(Icons.Default.Description, "DESCRIPCIÓN", SarcGreenLight, SarcGreen) {
                    Text(text = reporte.descripcion, fontSize = 14.sp, color = TextMid)
                }

                Spacer(Modifier.height(16.dp))

                if (!reporte.ubicacion.isNullOrBlank()) {
                    InfoSection(Icons.Default.LocationOn, "UBICACIÓN", SarcGreenLight, SarcGreen) {
                        Text(text = reporte.ubicacion, fontSize = 14.sp, color = TextMid)
                    }
                    Spacer(Modifier.height(16.dp))
                }

                InfoSection(Icons.Default.CalendarToday, "FECHA DE REPORTE", SarcGreenLight, SarcGreen) {
                    Text(text = reporte.fechaFormateada, fontSize = 14.sp, color = TextMid)
                }

                Spacer(Modifier.height(16.dp))

                InfoSection(Icons.Default.Person, "REPORTADO POR", SarcGreenLight, SarcGreen) {
                    Text(text = reporte.nombreUsuario, fontSize = 14.sp, color = TextMid)
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        BottomActions(
            reporte = reporte,
            onComoLlegar = onComoLlegar
        )
    }
}

@Composable
private fun ImagenConEstado(imagenUrl: String?, nombreEstado: String, estadoColor: Color) {
    Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        if (!imagenUrl.isNullOrBlank()) {
            AsyncImage(model = imagenUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        } else {
            Box(Modifier.fillMaxSize().background(FieldBackground), Alignment.Center) {
                Text("Sin imagen", color = TextLight, fontSize = 14.sp)
            }
        }
        Box(Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.15f)))))
        Box(Modifier.align(Alignment.TopEnd).padding(12.dp).clip(RoundedCornerShape(12.dp)).background(estadoColor).padding(horizontal = 14.dp, vertical = 6.dp)) {
            Text(text = nombreEstado, color = SurfaceWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CategoriaSection(nombreIncidencia: String, emoji: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(FieldBackground), Alignment.Center) {
            Text(text = emoji, fontSize = 18.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text("CATEGORÍA", fontSize = 11.sp, color = TextLight, fontWeight = FontWeight.SemiBold)
            Text(nombreIncidencia, fontSize = 16.sp, color = TextDark, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun InfoSection(icon: ImageVector, label: String, bgColor: Color, iconTint: Color, content: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(Modifier.size(36.dp).clip(CircleShape).background(bgColor), Alignment.Center) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextLight, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            content()
        }
    }
}

@Composable
private fun BottomActions(
    reporte: DetalleReporteUIModel,
    onComoLlegar: (Double, Double) -> Unit,
    viewModel: DetalleReporteAdminViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        EstadoSelectorDialog(
            estadoActualId = reporte.idEstado,
            onDismiss = { showDialog = false },
            onEstadoSelected = { nuevoId ->
                viewModel.actualizarEstado(reporte.idReporte, nuevoId)
                showDialog = false
            }
        )
    }

    Surface(Modifier.fillMaxWidth(), shadowElevation = 8.dp, color = SurfaceWhite) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { onComoLlegar(reporte.latitud ?: 0.0, reporte.longitud ?: 0.0) },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SarcGreen),
                enabled = reporte.latitud != null
            ) {
                Icon(Icons.Default.Navigation, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Cómo llegar", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SarcGreenDark)
            ) {
                Icon(Icons.Default.SwapHoriz, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Cambiar Estado", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EstadoSelectorDialog(estadoActualId: Int, onDismiss: () -> Unit, onEstadoSelected: (Int) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar Estado", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                EstadoOption(1, "Pendiente", OrangeWarning, estadoActualId == 1) { onEstadoSelected(1) }
                EstadoOption(2, "En Proceso", BlueProceso, estadoActualId == 2) { onEstadoSelected(2) }
                EstadoOption(3, "Resuelto", GreenResuelto, estadoActualId == 3) { onEstadoSelected(3) }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = SarcGreen) } }
    )
}

@Composable
private fun EstadoOption(id: Int, nombre: String, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp, horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(16.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(12.dp))
        Text(nombre, Modifier.weight(1f), fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) SarcGreen else TextDark)
        if (isSelected) Icon(Icons.Filled.Check, null, tint = SarcGreen, modifier = Modifier.size(20.dp))
    }
}
