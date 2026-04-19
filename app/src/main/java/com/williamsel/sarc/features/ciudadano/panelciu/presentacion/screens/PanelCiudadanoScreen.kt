package com.williamsel.sarc.features.ciudadano.panelciu.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.williamsel.sarc.features.ciudadano.panelciu.presentacion.viewmodels.PanelCiudadanoViewModel
import com.williamsel.sarc.ui.theme.*

@Composable
fun PanelCiudadanoScreen(
    onNavigateToCrearReporte: () -> Unit,
    onNavigateToMisReportes: () -> Unit,
    onNavigateToMapa: () -> Unit,
    onLogout: () -> Unit,
    viewModel: PanelCiudadanoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    PanelCiudadanoContent(
        state = state,
        onNavigateToCrearReporte = onNavigateToCrearReporte,
        onNavigateToMisReportes = onNavigateToMisReportes,
        onNavigateToMapa = onNavigateToMapa,
        onLogout = {
            viewModel.logout()
            onLogout()
        }
    )
}

@Composable
fun PanelCiudadanoContent(
    state: PanelCiudadanoUiState,
    onNavigateToCrearReporte: () -> Unit,
    onNavigateToMisReportes: () -> Unit,
    onNavigateToMapa: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
            .verticalScroll(rememberScrollState())
    ) {
        PanelTopBar(
            nombre   = state.nombreCompleto.ifBlank { "Ciudadano" },
            onLogout = onLogout
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CardWhite)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EstadisticaCard(
                        modifier    = Modifier.weight(1f),
                        label       = "Total\nReportes",
                        valor       = state.total.toString(),
                        icon        = Icons.Default.Description,
                        iconTint    = SarcGreen,
                        iconBgColor = CardMintBg,
                        valorColor  = TextDark
                    )
                    EstadisticaCard(
                        modifier    = Modifier.weight(1f),
                        label       = "Pendientes",
                        valor       = state.pendientes.toString(),
                        icon        = Icons.Default.Warning,
                        iconTint    = OrangeWarning,
                        iconBgColor = Color(0xFFFFF3E0),
                        valorColor  = OrangeWarning
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EstadisticaCard(
                        modifier    = Modifier.weight(1f),
                        label       = "En Proceso",
                        valor       = state.enProceso.toString(),
                        icon        = Icons.Default.Schedule,
                        iconTint    = BlueProceso,
                        iconBgColor = Color(0xFFE3F2FD),
                        valorColor  = BlueProceso
                    )
                    EstadisticaCard(
                        modifier    = Modifier.weight(1f),
                        label       = "Resueltos",
                        valor       = state.resueltos.toString(),
                        icon        = Icons.Default.CheckCircle,
                        iconTint    = GreenResuelto,
                        iconBgColor = Color(0xFFE8F5E9),
                        valorColor  = GreenResuelto
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            AccionCard(
                icon = Icons.Default.Add,
                iconBgColor = CardMintBg,
                iconTint = SarcGreen,
                titulo = "Reportar Incidencia",
                subtitulo = "Crea un nuevo reporte en segundos",
                onClick = onNavigateToCrearReporte
            )
            AccionCard(
                icon = Icons.Default.Description,
                iconBgColor = Color(0xFFEDE7F6),
                iconTint = BlueAccent,
                titulo = "Mis Reportes",
                subtitulo = "Ver historial completo",
                onClick = onNavigateToMisReportes
            )
            AccionCard(
                icon = Icons.Default.Map,
                iconBgColor = Color(0xFFF3E5F5),
                iconTint = PurpleAccent,
                titulo = "Mapa Interactivo",
                subtitulo = "Visualización geográfica",
                onClick = onNavigateToMapa
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PanelTopBar(nombre: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(DarkNavy),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "S", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = SarcGreen)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "SARC", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CardWhite)
            Text(text = nombre, fontSize = 13.sp, color = CardWhite.copy(alpha = 0.85f))
        }
        TextButton(onClick = onLogout) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir", tint = CardWhite, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Salir", color = CardWhite, fontSize = 14.sp)
        }
    }
}

@Composable
private fun EstadisticaCard(
    modifier: Modifier = Modifier,
    label: String,
    valor: String,
    icon: ImageVector,
    iconTint: Color,
    iconBgColor: Color,
    valorColor: Color
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = label, fontSize = 12.sp, color = TextMid, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = valor, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = valorColor)
            }
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
private fun AccionCard(
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = titulo, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                Text(text = subtitulo, fontSize = 12.sp, color = TextMid)
            }
        }
    }
}
