package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.ReportesAdminViewModel
import com.williamsel.sarc.ui.theme.*

@Composable
fun ReportesAdminScreen(
    onNavigateBack: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    viewModel: ReportesAdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
    ) {
        ReportesAdminTopBar(onBack = onNavigateBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = SurfaceWhite,
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
                )
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Buscar reportes...", color = TextLight) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = TextLight
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SarcGreen,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark,
                    cursorColor = SarcGreen,
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite
                )
            )

            FiltrosRow(
                estadoSeleccionado = uiState.estadoSeleccionado,
                onFiltroChange = { viewModel.onEstadoFilterChanged(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiState.reportes.size} reportes encontrados",
                fontSize = 13.sp,
                color = TextMid,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SarcGreen)
                }
            } else if (uiState.reportes.isEmpty()) {
                EstadoVacio()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.reportes,
                        key = { it.idReporte }
                    ) { reporte ->
                        ReporteAdminCard(
                            reporte = reporte,
                            onVerDetalle = onVerDetalle
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportesAdminTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = SurfaceWhite
            )
        }
        Text(
            text = "Todos los Reportes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = SurfaceWhite
        )
    }
}

@Composable
private fun FiltrosRow(
    estadoSeleccionado: EstadoFiltro,
    onFiltroChange: (EstadoFiltro) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EstadoFiltro.entries.forEach { filtro ->
            val activo = filtro == estadoSeleccionado
            val colorFondo = if (activo) {
                when (filtro) {
                    EstadoFiltro.PENDIENTES -> OrangeWarning
                    EstadoFiltro.EN_PROCESO -> BlueProceso
                    EstadoFiltro.RESUELTOS -> GreenResuelto
                    else -> SarcGreen
                }
            } else Color(0xFFF0F0F0)
            
            val colorTexto = if (activo) SurfaceWhite else TextMid

            FilterChip(
                selected = activo,
                onClick = { onFiltroChange(filtro) },
                label = {
                    Text(
                        text = filtro.label,
                        fontSize = 12.sp,
                        color = colorTexto
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colorFondo,
                    containerColor = Color(0xFFF0F0F0),
                    selectedLabelColor = SurfaceWhite,
                    labelColor = TextMid
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = activo,
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun ReporteAdminCard(
    reporte: ReporteAdminUiModel,
    onVerDetalle: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVerDetalle(reporte.idReporte) }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!reporte.imagen.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(reporte.imagen)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen del reporte",
                        modifier = Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = reporte.titulo,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark,
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        EstadoBadgeAdmin(nombre = reporte.nombreEstado, color = reporte.estadoColor)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = reporte.descripcion,
                        fontSize = 12.sp,
                        color = TextMid,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetaItemAdmin(
                    icon = Icons.Default.Category,
                    texto = reporte.nombreIncidencia,
                    modifier = Modifier.weight(1f)
                )
                MetaItemAdmin(
                    icon = Icons.Default.Person,
                    texto = reporte.nombreUsuario,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextLight,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reporte.ubicacion,
                        fontSize = 11.sp,
                        color = TextMid,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = TextLight,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reporte.fechaFormateada,
                        fontSize = 11.sp,
                        color = TextLight
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoBadgeAdmin(nombre: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color
    ) {
        Text(
            text = nombre,
            fontSize = 10.sp,
            color = SurfaceWhite,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun MetaItemAdmin(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextLight,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = texto,
            fontSize = 11.sp,
            color = TextMid,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EstadoVacio() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No se encontraron reportes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextMid
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Intenta con otros filtros o términos",
                fontSize = 13.sp,
                color = TextLight
            )
        }
    }
}
