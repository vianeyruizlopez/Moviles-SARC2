package com.williamsel.sarc.features.superadmin.usuariosuperadmin.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupervisedUserCircle
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
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.RolUsuario
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.domain.entities.UsuarioSistema
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.presentacion.viewmodels.UsuarioSuperAdminUiState
import com.williamsel.sarc.features.superadmin.usuariosuperadmin.presentacion.viewmodels.UsuarioSuperAdminViewModel
import com.williamsel.sarc.ui.theme.BlueProceso
import com.williamsel.sarc.ui.theme.CardWhite
import com.williamsel.sarc.ui.theme.GreenResuelto
import com.williamsel.sarc.ui.theme.OrangeWarning
import com.williamsel.sarc.ui.theme.PurpleAccent
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.SarcTheme
import com.williamsel.sarc.ui.theme.SurfaceWhite
import com.williamsel.sarc.ui.theme.TextDark
import com.williamsel.sarc.ui.theme.TextLight
import com.williamsel.sarc.ui.theme.TextMid

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun UsuarioSuperAdminScreen(
    onBack: () -> Unit = {},
    viewModel: UsuarioSuperAdminViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    state.errorMessage?.let { LaunchedEffect(it) { viewModel.clearError() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
    ) {
        TopBarUsuarios(onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F0FF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxWidth().height(200.dp), Alignment.Center) {
                    CircularProgressIndicator(color = SarcGreen)
                }
            } else {
                // ── Tarjetas resumen ──────────────────────────────────────────
                TarjetaConteo(
                    label  = "Total Usuarios",
                    valor  = state.totalUsuarios.toString(),
                    icon   = Icons.Default.SupervisedUserCircle,
                    color  = Color(0xFF1565C0)
                )
                TarjetaConteo(
                    label  = "Administradores",
                    valor  = state.totalAdministradores.toString(),
                    icon   = Icons.Default.Shield,
                    color  = Color(0xFFD32F2F)
                )
                TarjetaConteo(
                    label  = "Ciudadanos",
                    valor  = state.totalCiudadanos.toString(),
                    icon   = Icons.Default.PersonOutline,
                    color  = Color(0xFF1976D2)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ── Sección Administradores ───────────────────────────────────
                SeccionUsuarios(
                    titulo        = "Administradores",
                    icono         = Icons.Default.Shield,
                    iconoColor    = Color(0xFFD32F2F),
                    fondoColor    = Color(0xFFFFF0F0),
                    usuarios      = state.administradores,
                    onToggleActivo = { viewModel.toggleActivo(it) }
                )

                // ── Sección Ciudadanos ────────────────────────────────────────
                SeccionUsuarios(
                    titulo        = "Ciudadanos",
                    icono         = Icons.Default.Person,
                    iconoColor    = Color(0xFF1565C0),
                    fondoColor    = Color(0xFFF0F4FF),
                    usuarios      = state.ciudadanos,
                    onToggleActivo = { viewModel.toggleActivo(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── TopBar ────────────────────────────────────────────────────────────────────
@Composable
private fun TopBarUsuarios(onBack: () -> Unit) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = SurfaceWhite)
        }
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.SupervisedUserCircle,
                    contentDescription = null,
                    tint     = SurfaceWhite,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text       = "Todos los Usuarios",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = SurfaceWhite
                )
            }
            Text(
                text     = "Gestión de usuarios del sistema",
                fontSize = 12.sp,
                color    = SurfaceWhite.copy(alpha = 0.8f)
            )
        }
    }
}

// ── Tarjeta de conteo ─────────────────────────────────────────────────────────
@Composable
private fun TarjetaConteo(
    label: String,
    valor: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(label, fontSize = 13.sp, color = TextMid)
                Text(
                    valor,
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark
                )
            }
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = color,
                modifier           = Modifier.size(32.dp)
            )
        }
    }
}

// ── Sección de usuarios (Admins o Ciudadanos) ─────────────────────────────────
@Composable
private fun SeccionUsuarios(
    titulo: String,
    icono: ImageVector,
    iconoColor: Color,
    fondoColor: Color,
    usuarios: List<UsuarioSistema>,
    onToggleActivo: (UsuarioSistema) -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado de sección
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .background(fondoColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icono, null, tint = iconoColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = titulo,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (usuarios.isEmpty()) {
                Text(
                    text     = "Sin usuarios registrados",
                    fontSize = 13.sp,
                    color    = TextLight,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                usuarios.forEach { usuario ->
                    UsuarioItem(
                        usuario        = usuario,
                        onToggleActivo = { onToggleActivo(usuario) }
                    )
                    if (usuario != usuarios.last()) {
                        HorizontalDivider(
                            color    = Color(0xFFF0F0F0),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Item de usuario ───────────────────────────────────────────────────────────
@Composable
private fun UsuarioItem(
    usuario: UsuarioSistema,
    onToggleActivo: () -> Unit
) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar con color según rol
        val (avatarBg, avatarTint) = when (usuario.rol) {
            RolUsuario.SUPER_ADMIN -> Color(0xFFF3E5F5) to PurpleAccent
            RolUsuario.ADMIN       -> Color(0xFFFFF0F0) to Color(0xFFD32F2F)
            RolUsuario.CIUDADANO   -> Color(0xFFE3F2FD) to BlueProceso
        }

        Box(
            modifier         = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(avatarBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint     = avatarTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = usuario.nombreCompleto,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextDark
            )
            Text(
                text     = usuario.email,
                fontSize = 12.sp,
                color    = TextMid
            )
        }

        // Badge de rol
        RolBadge(rol = usuario.rol)

        Spacer(modifier = Modifier.width(6.dp))

        // Badge activo/inactivo — clickeable para toggle
        Surface(
            onClick = onToggleActivo,
            shape   = RoundedCornerShape(20.dp),
            color   = if (usuario.activo) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
        ) {
            Text(
                text       = if (usuario.activo) "Activo" else "Inactivo",
                fontSize   = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color      = if (usuario.activo) GreenResuelto else OrangeWarning,
                modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

// ── Badge de rol ──────────────────────────────────────────────────────────────
@Composable
private fun RolBadge(rol: RolUsuario) {
    val (bg, textColor) = when (rol) {
        RolUsuario.SUPER_ADMIN -> Color(0xFFEDE7F6) to PurpleAccent
        RolUsuario.ADMIN       -> Color(0xFFE3F2FD) to BlueProceso
        RolUsuario.CIUDADANO   -> Color(0xFFE8F5E9) to GreenResuelto
    }
    Surface(shape = RoundedCornerShape(20.dp), color = bg) {
        Text(
            text       = rol.label,
            fontSize   = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color      = textColor,
            modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// ── Texto helper (evita ambigüedad con Text de M3) ────────────────────────────
@Composable
private fun texto(texto: String, fontSize: androidx.compose.ui.unit.TextUnit, fontWeight: FontWeight, color: Color) {
    Text(text = texto, fontSize = fontSize, fontWeight = fontWeight, color = color)
}

// ── Previews ──────────────────────────────────────────────────────────────────
private val usuariosFake = listOf(
    UsuarioSistema(1, "Super Administrador", "superadmin@sarc.com", RolUsuario.SUPER_ADMIN, true),
    UsuarioSistema(2, "Administrador Municipal", "admin@sarc.com", RolUsuario.ADMIN, true)
)
private val ciudadanosFake = listOf(
    UsuarioSistema(3, "Juan Pérez", "ciudadano@sarc.com", RolUsuario.CIUDADANO, true)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    SarcTheme {
        UsuarioSuperAdminContent(
            state = UsuarioSuperAdminUiState(
                totalUsuarios        = 3,
                totalAdministradores = 2,
                totalCiudadanos      = 1,
                administradores      = usuariosFake,
                ciudadanos           = ciudadanosFake
            )
        )
    }
}

// Composable interno para preview sin ViewModel
@Composable
internal fun UsuarioSuperAdminContent(
    state: UsuarioSuperAdminUiState,
    onBack: () -> Unit = {},
    onToggleActivo: (UsuarioSistema) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(SarcGreen)) {
        TopBarUsuarios(onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F0FF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaConteo("Total Usuarios",    state.totalUsuarios.toString(),        Icons.Default.SupervisedUserCircle, Color(0xFF1565C0))
            TarjetaConteo("Administradores",   state.totalAdministradores.toString(), Icons.Default.Shield,               Color(0xFFD32F2F))
            TarjetaConteo("Ciudadanos",        state.totalCiudadanos.toString(),      Icons.Default.PersonOutline,         Color(0xFF1976D2))
            Spacer(modifier = Modifier.height(4.dp))
            SeccionUsuarios("Administradores", Icons.Default.Shield,    Color(0xFFD32F2F), Color(0xFFFFF0F0), state.administradores, onToggleActivo)
            SeccionUsuarios("Ciudadanos",      Icons.Default.Person,    Color(0xFF1565C0), Color(0xFFF0F4FF), state.ciudadanos,       onToggleActivo)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
