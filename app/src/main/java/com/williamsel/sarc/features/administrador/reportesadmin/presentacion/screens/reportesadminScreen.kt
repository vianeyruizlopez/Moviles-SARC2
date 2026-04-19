package com.williamsel.sarc.features.administrador.reportesadmin.presentacion.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.EstadoFiltro
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.ReporteAdminUiModel
import com.williamsel.sarc.features.administrador.reportesadmin.presentacion.viewmodels.ReportesAdminViewModel
import com.williamsel.sarc.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesAdminScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportesAdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) }
            )

            FilterTabsSection(
                estadoSeleccionado = uiState.estadoSeleccionado,
                onFiltroChange = { viewModel.onEstadoFilterChanged(it) },
                viewModel = viewModel
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SarcGreen)
                }
            } else if (uiState.errorMessage != null) {
                ErrorContent(
                    message = uiState.errorMessage!!,
                    onRetry = { viewModel.cargarReportes() }
                )
            } else {
                Text(
                    text = "${uiState.reportes.size} reportes encontrados",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 13.sp,
                    color = TextMid,
                    fontWeight = FontWeight.Medium
                )

                if (uiState.reportes.isEmpty()) {
                    EmptyContent()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = uiState.reportes, key = { it.idReporte }) { reporte ->
                            ReporteCard(reporte = reporte)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth().padding(16.dp, 10.dp),
        placeholder = { Text("Buscar reportes...", color = TextLight) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = TextLight) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceWhite,
            unfocusedContainerColor = SurfaceWhite,
            focusedBorderColor = SarcGreen,
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
private fun FilterTabsSection(
    estadoSeleccionado: EstadoFiltro,
    onFiltroChange: (EstadoFiltro) -> Unit,
    viewModel: ReportesAdminViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EstadoFiltro.entries.forEach { filtro ->
            val isSelected = filtro == estadoSeleccionado
            val bgColor by animateColorAsState(if (isSelected) viewModel.getEstadoColor(filtro) else SurfaceWhite, label = "")
            val textColor by animateColorAsState(if (isSelected) SurfaceWhite else TextMid, label = "")

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .border(1.dp, if (isSelected) Color.Transparent else FieldBackground, RoundedCornerShape(20.dp))
                    .clickable { onFiltroChange(filtro) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(filtro.label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
    }
}

@Composable
private fun ReporteCard(reporte: ReporteAdminUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.width(5.dp).fillMaxHeight().background(reporte.estadoColor))
            Column(modifier = Modifier.padding(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(reporte.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    EstadoBadge(reporte.nombreEstado, reporte.estadoColor)
                }
                Text(reporte.descripcion, fontSize = 13.sp, color = TextMid, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoChip(Icons.Default.Category, reporte.nombreIncidencia)
                    InfoChip(Icons.Default.Person, reporte.nombreUsuario)
                }
                if (!reporte.ubicacion.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoChip(Icons.Default.LocationOn, reporte.ubicacion)
                }
                Spacer(modifier = Modifier.height(4.dp))
                InfoChip(Icons.Default.CalendarToday, reporte.fechaFormateada, color = TextLight)
            }
        }
    }
}

@Composable
private fun EstadoBadge(nombre: String, color: Color) {
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(color).padding(6.dp, 2.dp)) {
        Text(nombre, color = SurfaceWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color = TextMid) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 11.sp, color = color, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(message, color = ErrorRed)
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = SarcGreen)) { Text("Reintentar") }
    }
}

@Composable
private fun EmptyContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay reportes que coincidan", color = TextLight)
    }
}
