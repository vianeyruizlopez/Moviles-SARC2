package com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.williamsel.sarc.features.administrador.detallereporteadmin.domain.entities.DetalleReporteAdmin
import com.williamsel.sarc.features.administrador.detallereporteadmin.presentacion.viewmodels.DetalleReporteAdminViewModel
import com.williamsel.sarc.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleReporteAdminScreen(
    idReporte: Int,
    onNavigateBack: () -> Unit,
    onComoLlegar: (Double, Double) -> Unit,
    onCambiarEstado: (Int, Int) -> Unit, // idReporte, idEstadoActual
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
                    Text(
                        text = "Detalle del Reporte",
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
        when (val state = uiState) {
            is DetalleReporteAdminUIState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SarcGreen)
                }
            }

            is DetalleReporteAdminUIState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = ErrorRed,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.reintentar() },
                            colors = ButtonDefaults.buttonColors(containerColor = SarcGreen)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is DetalleReporteAdminUIState.Success -> {
                DetalleContent(
                    reporte = state.reporte,
                    paddingValues = paddingValues,
                    onComoLlegar = onComoLlegar,
                    onCambiarEstado = onCambiarEstado
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Contenido principal del detalle
// ═══════════════════════════════════════════════════════════════

@Composable
private fun DetalleContent(
    reporte: DetalleReporteAdmin,
    paddingValues: PaddingValues,
    onComoLlegar: (Double, Double) -> Unit,
    onCambiarEstado: (Int, Int) -> Unit
) {
    val estadoColor = getEstadoColorById(reporte.idEstado)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Imagen con badge de estado superpuesto ──
            ImagenConEstado(
                imagenUrl = reporte.imagen,
                nombreEstado = reporte.nombreEstado,
                estadoColor = estadoColor
            )

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // ── Categoría ──
                CategoriaSection(nombreIncidencia = reporte.nombreIncidencia)

                Spacer(modifier = Modifier.height(16.dp))

                // ── Título ──
                Text(
                    text = reporte.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextDark,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Descripción ──
                InfoSection(
                    icon = Icons.Default.Description,
                    label = "DESCRIPCIÓN",
                    iconBgColor = SarcGreenLight,
                    iconTint = SarcGreen
                ) {
                    Text(
                        text = reporte.descripcion,
                        fontSize = 14.sp,
                        color = TextMid,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Ubicación ──
                if (!reporte.ubicacion.isNullOrBlank()) {
                    InfoSection(
                        icon = Icons.Default.LocationOn,
                        label = "UBICACIÓN",
                        iconBgColor = SarcGreenLight,
                        iconTint = SarcGreen
                    ) {
                        Text(
                            text = reporte.ubicacion,
                            fontSize = 14.sp,
                            color = TextMid,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ── Fecha de reporte ──
                InfoSection(
                    icon = Icons.Default.CalendarToday,
                    label = "FECHA DE REPORTE",
                    iconBgColor = SarcGreenLight,
                    iconTint = SarcGreen
                ) {
                    Text(
                        text = formatFechaDetalle(reporte.fecha),
                        fontSize = 14.sp,
                        color = TextMid
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Reportado por ──
                InfoSection(
                    icon = Icons.Default.Person,
                    label = "REPORTADO POR",
                    iconBgColor = SarcGreenLight,
                    iconTint = SarcGreen
                ) {
                    Text(
                        text = reporte.nombreUsuario,
                        fontSize = 14.sp,
                        color = TextMid,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // ── Botones inferiores ──
        BottomActions(
            reporte = reporte,
            onComoLlegar = onComoLlegar,
            onCambiarEstado = onCambiarEstado
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Imagen con badge de estado
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ImagenConEstado(
    imagenUrl: String?,
    nombreEstado: String,
    estadoColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        if (!imagenUrl.isNullOrBlank()) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = "Imagen del reporte",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder cuando no hay imagen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FieldBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin imagen",
                    color = TextLight,
                    fontSize = 14.sp
                )
            }
        }

        // Gradiente sutil en la parte inferior de la imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.15f))
                    )
                )
        )

        // Badge de estado
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(estadoColor)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = nombreEstado,
                color = SurfaceWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Sección de categoría con icono
// ═══════════════════════════════════════════════════════════════

@Composable
private fun CategoriaSection(nombreIncidencia: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Icono circular de categoría (como en el screenshot con el ícono de construcción)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(FieldBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getCategoriaEmoji(nombreIncidencia),
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = "CATEGORÍA",
                fontSize = 11.sp,
                color = TextLight,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Text(
                text = nombreIncidencia,
                fontSize = 16.sp,
                color = TextDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Sección de info genérica (Descripción, Ubicación, Fecha, etc.)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun InfoSection(
    icon: ImageVector,
    label: String,
    iconBgColor: Color,
    iconTint: Color,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextLight,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Botones inferiores: Cómo llegar + Cambiar Estado
// ═══════════════════════════════════════════════════════════════

@Composable
private fun BottomActions(
    reporte: DetalleReporteAdmin,
    onComoLlegar: (Double, Double) -> Unit,
    onCambiarEstado: (Int, Int) -> Unit,
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = SurfaceWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón "Cómo llegar"
            Button(
                onClick = {
                    val lat = reporte.latitud ?: 0.0
                    val lng = reporte.longitud ?: 0.0
                    onComoLlegar(lat, lng)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SarcGreen
                ),
                enabled = reporte.latitud != null && reporte.longitud != null
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Cómo llegar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Botón "Cambiar Estado"
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SarcGreenDark
                )
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Cambiar Estado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun EstadoSelectorDialog(
    estadoActualId: Int,
    onDismiss: () -> Unit,
    onEstadoSelected: (Int) -> Unit
) {
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
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = SarcGreen)
            }
        }
    )
}

@Composable
private fun EstadoOption(
    id: Int,
    nombre: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = nombre,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) SarcGreen else TextDark
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = SarcGreen,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Utilidades
// ═══════════════════════════════════════════════════════════════

private fun getEstadoColorById(idEstado: Int): Color {
    return when (idEstado) {
        1    -> OrangeWarning   // Pendiente
        2    -> BlueProceso     // En Proceso
        3    -> GreenResuelto   // Resuelto
        else -> TextMid
    }
}

private fun getCategoriaEmoji(nombreIncidencia: String): String {
    return when (nombreIncidencia.lowercase()) {
        "bache"       -> "\uD83D\uDEA7"  // 🚧
        "basura"      -> "\uD83D\uDDD1"  // 🗑
        "alumbrado"   -> "\uD83D\uDCA1"  // 💡
        "fuga de agua", "fuga" -> "\uD83D\uDCA7" // 💧
        "alcantarilla"-> "\uD83D\uDEBF"  // 🚿
        else          -> "\uD83D\uDCCB"  // 📋
    }
}

private fun formatFechaDetalle(fechaStr: String): String {
    return try {
        val dateTime = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern(
            "dd 'de' MMMM, yyyy 'a las' HH:mm",
            Locale("es", "MX")
        )
        dateTime.format(formatter)
    } catch (e: Exception) {
        fechaStr
    }
}
