package com.williamsel.sarc.features.administrador.paneladmin.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.williamsel.sarc.features.administrador.paneladmin.presentacion.viewmodels.PanelPrincipalAdminViewModel
import com.williamsel.sarc.ui.theme.*

@Composable
fun PanelPrincipalAdminScreen(
    onNavegaReportes:     () -> Unit = {},
    onNavegaMapa:         () -> Unit = {},
    onNavegaAdminPanel:   () -> Unit = {},
    onSalir:              () -> Unit = {},
    viewModel: PanelPrincipalAdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
    ) {
        PanelTopBar(onSalir = onSalir)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(BackgroundLight)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            when (val state = uiState) {
                is PanelPrincipalAdminUIState.Loading -> {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = SarcGreen)
                    }
                }
                is PanelPrincipalAdminUIState.Error -> {
                    ErrorCard(mensaje = state.mensaje, onReintentar = viewModel::cargarResumen)
                }
                is PanelPrincipalAdminUIState.Success -> {
                    EstadisticasGrid(uiModel = state.uiModel)
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE0E8E0), thickness = 1.dp)
            Spacer(Modifier.height(16.dp))

            NavCard(
                icono          = Icons.Default.Description,
                iconoColor     = SarcGreen,
                iconoBg        = CardMintBg,
                titulo         = "Todos los Reportes",
                subtitulo      = "Ver historial completo",
                onClick        = onNavegaReportes
            )
            Spacer(Modifier.height(10.dp))
            NavCard(
                icono          = Icons.Default.Map,
                iconoColor     = PurpleAccent,
                iconoBg        = Color(0xFFEDE7F6),
                titulo         = "Mapa Interactivo",
                subtitulo      = "Visualización geográfica",
                onClick        = onNavegaMapa
            )
            Spacer(Modifier.height(10.dp))
            NavCard(
                icono          = Icons.Default.Shield,
                iconoColor     = ErrorRed,
                iconoBg        = Color(0xFFFCE4EC),
                titulo         = "Panel de Administración",
                subtitulo      = "Gestionar incidencias",
                onClick        = onNavegaAdminPanel
            )
        }
    }
}


@Composable
private fun PanelTopBar(onSalir: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier        = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Column {
                Text("SARC",                   color = Color.White, fontWeight = FontWeight.Bold,   fontSize = 18.sp)
                Text("Administrador Municipal", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
        OutlinedButton(
            onClick  = onSalir,
            shape    = RoundedCornerShape(20.dp),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border   = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Salir", fontSize = 13.sp)
        }
    }
}


@Composable
private fun EstadisticasGrid(uiModel: PanelPrincipalUIModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                label  = "Total\nRepor.",
                valor  = uiModel.totalReportes,
                icono  = Icons.Default.Description,
                iconoBg = Color(0xFFE8F0FE),
                iconoColor = BlueProceso,
                valorColor = TextDark,
                modifier   = Modifier.weight(1f)
            )
            StatCard(
                label  = "Pendientes",
                valor  = uiModel.pendientes,
                icono  = Icons.Default.Warning,
                iconoBg    = Color(0xFFFFF3E0),
                iconoColor = OrangeWarning,
                valorColor = OrangeWarning,
                modifier   = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                label  = "En Proceso",
                valor  = uiModel.enProceso,
                icono  = Icons.Default.PlayCircle,
                iconoBg    = Color(0xFFE0F7F4),
                iconoColor = BlueProceso,
                valorColor = BlueProceso,
                modifier   = Modifier.weight(1f)
            )
            StatCard(
                label  = "Resueltos",
                valor  = uiModel.resueltos,
                icono  = Icons.Default.CheckCircle,
                iconoBg    = CardMintBg,
                iconoColor = GreenResuelto,
                valorColor = GreenResuelto,
                modifier   = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun StatCard(
    label:      String,
    valor:      String,
    icono:      ImageVector,
    iconoBg:    Color,
    iconoColor: Color,
    valorColor: Color,
    modifier:   Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier                = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement   = Arrangement.SpaceBetween,
            verticalAlignment       = Alignment.Top
        ) {
            Column {
                Text(
                    text       = label,
                    fontSize   = 11.sp,
                    color      = TextLight,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text       = valor,
                    fontSize   = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = valorColor
                )
            }
            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconoBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = iconoColor, modifier = Modifier.size(20.dp))
            }
        }
    }
}


@Composable
private fun NavCard(
    icono:     ImageVector,
    iconoColor: Color,
    iconoBg:   Color,
    titulo:    String,
    subtitulo: String,
    onClick:   () -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth().clickable { onClick() },
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier         = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconoBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = iconoColor, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo,    fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextDark)
                Text(subtitulo, fontSize   = 12.sp,               color = TextLight)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextLight)
        }
    }
}


@Composable
private fun ErrorCard(mensaje: String, onReintentar: () -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC))
    ) {
        Column(
            modifier              = Modifier.padding(20.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(40.dp))
            Text(mensaje, color = ErrorRed, fontSize = 14.sp)
            Button(
                onClick = onReintentar,
                colors  = ButtonDefaults.buttonColors(containerColor = SarcGreen)
            ) {
                Text("Reintentar")
            }
        }
    }
}
