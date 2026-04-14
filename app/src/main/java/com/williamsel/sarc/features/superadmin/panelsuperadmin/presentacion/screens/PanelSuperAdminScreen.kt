package com.williamsel.sarc.features.superadmin.panelsuperadmin.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.williamsel.sarc.features.superadmin.panelsuperadmin.presentacion.viewmodels.PanelSuperAdminUiState
import com.williamsel.sarc.features.superadmin.panelsuperadmin.presentacion.viewmodels.PanelSuperAdminViewModel
import com.williamsel.sarc.ui.theme.BlueProceso
import com.williamsel.sarc.ui.theme.CardWhite
import com.williamsel.sarc.ui.theme.CardMintBg
import com.williamsel.sarc.ui.theme.DarkNavy
import com.williamsel.sarc.ui.theme.GreenResuelto
import com.williamsel.sarc.ui.theme.OrangeWarning
import com.williamsel.sarc.ui.theme.PurpleAccent
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.SarcTheme
import com.williamsel.sarc.ui.theme.SurfaceWhite
import com.williamsel.sarc.ui.theme.TextDark
import com.williamsel.sarc.ui.theme.TextMid

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun PanelSuperAdminScreen(
    onNavigateToCrearAdmin: () -> Unit = {},
    onNavigateToUsuarios: () -> Unit = {},
    onNavigateToEstadisticas: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: PanelSuperAdminViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    state.errorMessage?.let { LaunchedEffect(it) { viewModel.clearError() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
            .verticalScroll(rememberScrollState())
    ) {
        // ── TopBar ────────────────────────────────────────────────────────────
        SuperAdminTopBar(
            nombre   = state.nombreCompleto.ifBlank { "Super Administrador" },
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
                Box(
                    modifier         = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CardWhite)
                }
            } else {
                // ── Fila 1: Total Reportes y Pendientes ───────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EstadisticaCard(
                        modifier    = Modifier.weight(1f),
                        label       = "Total Reportes",
                        valor       = state.totalReportes.toString(),
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

                // ── Fila 2: En Proceso y Resueltos ────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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

            // ── Acciones de Super Admin ───────────────────────────────────────
            AccionCard(
                icon        = Icons.Default.PersonAdd,
                iconBgColor = Color(0xFFEDE7F6),
                iconTint    = PurpleAccent,
                titulo      = "Crear Administrador",
                subtitulo   = "Agregar nuevo administrador",
                onClick     = onNavigateToCrearAdmin
            )

            AccionCard(
                icon        = Icons.Default.People,
                iconBgColor = Color(0xFFE3F2FD),
                iconTint    = BlueProceso,
                titulo      = "Todos los Usuarios",
                subtitulo   = "${state.totalAdmins} admins, ${state.totalCiudadanos} ciudadanos",
                onClick     = onNavigateToUsuarios
            )

            AccionCard(
                icon        = Icons.Default.BarChart,
                iconBgColor = CardMintBg,
                iconTint    = SarcGreen,
                titulo      = "Estadísticas Globales",
                subtitulo   = "Gráficas y análisis del sistema",
                onClick     = onNavigateToEstadisticas
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── TopBar ────────────────────────────────────────────────────────────────────
@Composable
private fun SuperAdminTopBar(
    nombre: String,
    onLogout: () -> Unit
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Box(
            modifier         = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(DarkNavy),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = "S",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = SarcGreen
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = "SARC",
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = SurfaceWhite
            )
            Text(
                text     = nombre,
                fontSize = 13.sp,
                color    = SurfaceWhite.copy(alpha = 0.85f)
            )
        }

        TextButton(onClick = onLogout) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Salir",
                tint               = SurfaceWhite,
                modifier           = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Salir", color = SurfaceWhite, fontSize = 14.sp)
        }
    }
}

// ── Tarjeta estadística ───────────────────────────────────────────────────────
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
        modifier  = modifier.height(90.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(label, fontSize = 12.sp, color = TextMid, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text       = valor,
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color      = valorColor
                )
            }
            Box(
                modifier         = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            }
        }
    }
}

// ── Tarjeta de acción ─────────────────────────────────────────────────────────
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
        onClick   = onClick,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(titulo, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                Text(subtitulo, fontSize = 12.sp, color = TextMid)
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    SarcTheme {
        PanelSuperAdminContent(
            state = PanelSuperAdminUiState(
                nombreCompleto  = "Super Administrador",
                totalReportes   = 0,
                pendientes      = 0,
                enProceso       = 0,
                resueltos       = 0,
                totalAdmins     = 2,
                totalCiudadanos = 1
            )
        )
    }
}

// Composable interno para preview sin ViewModel
@Composable
internal fun PanelSuperAdminContent(
    state: PanelSuperAdminUiState,
    onNavigateToCrearAdmin: () -> Unit = {},
    onNavigateToUsuarios: () -> Unit = {},
    onNavigateToEstadisticas: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
            .verticalScroll(rememberScrollState())
    ) {
        SuperAdminTopBar(
            nombre   = state.nombreCompleto.ifBlank { "Super Administrador" },
            onLogout = onLogout
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier            = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                EstadisticaCard(Modifier.weight(1f), "Total Reportes", state.totalReportes.toString(), Icons.Default.Description, SarcGreen, CardMintBg, TextDark)
                EstadisticaCard(Modifier.weight(1f), "Pendientes", state.pendientes.toString(), Icons.Default.Warning, OrangeWarning, Color(0xFFFFF3E0), OrangeWarning)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                EstadisticaCard(Modifier.weight(1f), "En Proceso", state.enProceso.toString(), Icons.Default.Schedule, BlueProceso, Color(0xFFE3F2FD), BlueProceso)
                EstadisticaCard(Modifier.weight(1f), "Resueltos", state.resueltos.toString(), Icons.Default.CheckCircle, GreenResuelto, Color(0xFFE8F5E9), GreenResuelto)
            }
            Spacer(modifier = Modifier.height(4.dp))
            AccionCard(Icons.Default.PersonAdd, Color(0xFFEDE7F6), PurpleAccent, "Crear Administrador", "Agregar nuevo administrador", onNavigateToCrearAdmin)
            AccionCard(Icons.Default.People, Color(0xFFE3F2FD), BlueProceso, "Todos los Usuarios", "${state.totalAdmins} admins, ${state.totalCiudadanos} ciudadanos", onNavigateToUsuarios)
            AccionCard(Icons.Default.BarChart, CardMintBg, SarcGreen, "Estadísticas Globales", "Gráficas y análisis del sistema", onNavigateToEstadisticas)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
