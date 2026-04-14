package com.williamsel.sarc.features.superadmin.crearusuario.presentacion.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.williamsel.sarc.R
import com.williamsel.sarc.features.superadmin.crearusuario.domain.entities.Administrador
import com.williamsel.sarc.features.superadmin.crearusuario.presentacion.viewmodels.CrearUsuarioUiState
import com.williamsel.sarc.features.superadmin.crearusuario.presentacion.viewmodels.CrearUsuarioViewModel
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.SarcTheme
import com.williamsel.sarc.ui.theme.SurfaceWhite
import com.williamsel.sarc.ui.theme.TextDark
import com.williamsel.sarc.ui.theme.TextLight
import com.williamsel.sarc.ui.theme.TextMid

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun CrearUsuarioScreen(
    onBack: () -> Unit = {},
    viewModel: CrearUsuarioViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Limpiar éxito
    LaunchedEffect(state.creadoExitoso) {
        if (state.creadoExitoso) viewModel.clearExito()
    }

    // Diálogo de confirmación eliminar
    state.adminAEliminar?.let { admin ->
        AlertDialog(
            onDismissRequest = viewModel::cancelarEliminar,
            title   = { Text("Eliminar administrador") },
            text    = { Text("¿Seguro que deseas eliminar a ${admin.nombreCompleto}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = viewModel::eliminarAdministrador,
                    colors  = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelarEliminar) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
    ) {
        // ── TopBar ────────────────────────────────────────────────────────────
        CrearUsuarioTopBar(
            onBack          = onBack,
            onCrearClick    = viewModel::mostrarFormulario,
            mostrarFormulario = state.mostrarFormulario
        )

        // ── Contenido ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFFF2F0FF),
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Formulario colapsable ─────────────────────────────────────────
            AnimatedVisibility(
                visible = state.mostrarFormulario,
                enter   = expandVertically(),
                exit    = shrinkVertically()
            ) {
                FormularioAdminCard(
                    state           = state,
                    onNombreChange      = viewModel::onNombreChange,
                    onEmailChange       = viewModel::onEmailChange,
                    onContrasenaChange  = viewModel::onContrasenaChange,
                    onCrear             = viewModel::crearAdministrador,
                    onCancelar          = viewModel::ocultarFormulario
                )
            }

            // ── Lista de administradores ──────────────────────────────────────
            Card(
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier  = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text       = "Administradores del Sistema",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextDark
                    )

                    if (state.isLoadingLista) {
                        // ── Lottie cargandoVerde.json ─────────────────────────
                        Box(
                            modifier         = Modifier.fillMaxWidth().height(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieLoading()
                        }
                    } else if (state.administradores.isEmpty()) {
                        Text(
                            text     = "No hay administradores registrados",
                            fontSize = 13.sp,
                            color    = TextLight,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        state.administradores.forEach { admin ->
                            AdminItem(
                                admin           = admin,
                                onToggleActivo  = { viewModel.toggleActivo(admin) },
                                onEliminar      = { viewModel.confirmarEliminar(admin) }
                            )
                        }
                    }
                }
            }

            // Error global
            state.errorMessage?.let { msg ->
                LaunchedEffect(msg) { viewModel.clearError() }
                Text(text = msg, fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── TopBar ────────────────────────────────────────────────────────────────────
@Composable
private fun CrearUsuarioTopBar(
    onBack: () -> Unit,
    onCrearClick: () -> Unit,
    mostrarFormulario: Boolean
) {
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = "Crear Administrador",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = SurfaceWhite
            )
            Text(
                text     = "Super Administrador",
                fontSize = 12.sp,
                color    = SurfaceWhite.copy(alpha = 0.8f)
            )
        }
        if (!mostrarFormulario) {
            Button(
                onClick = onCrearClick,
                shape   = RoundedCornerShape(20.dp),
                colors  = ButtonDefaults.buttonColors(containerColor = SurfaceWhite),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    Icons.Default.PersonAdd,
                    contentDescription = null,
                    tint     = SarcGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text       = "Crear Administrador",
                    color      = SarcGreen,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ── Formulario ────────────────────────────────────────────────────────────────
@Composable
private fun FormularioAdminCard(
    state: CrearUsuarioUiState,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onCrear: () -> Unit,
    onCancelar: () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text       = "Nuevo Administrador",
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextDark
            )

            // Nombre
            FormField(
                label         = "Nombre Completo",
                value         = state.nombreCompleto,
                onValueChange = onNombreChange,
                placeholder   = "Ej: Carlos García",
                error         = state.errorNombre
            )

            // Email
            FormField(
                label         = "Email",
                value         = state.email,
                onValueChange = onEmailChange,
                placeholder   = "Ej: carlos@sarc.com",
                keyboardType  = KeyboardType.Email,
                error         = state.errorEmail
            )

            // Contraseña
            FormField(
                label         = "Contraseña",
                value         = state.contrasena,
                onValueChange = onContrasenaChange,
                placeholder   = "Mínimo 6 caracteres",
                isPassword    = true,
                error         = state.errorContrasena
            )

            // Botones
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick  = onCrear,
                    enabled  = !state.isCreando,
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = SarcGreen)
                ) {
                    if (state.isCreando) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(18.dp),
                            color       = SurfaceWhite,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Crear Administrador",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                OutlinedButton(
                    onClick  = onCancelar,
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextMid),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDDDDD))
                ) {
                    Text("Cancelar", fontSize = 13.sp)
                }
            }
        }
    }
}

// ── Campo de formulario ───────────────────────────────────────────────────────
@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    error: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextDark)
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = TextLight, fontSize = 13.sp) },
            singleLine    = true,
            isError       = error != null,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else
                androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape   = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors  = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = SarcGreen,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedTextColor     = TextDark,
                unfocusedTextColor   = TextDark,
                cursorColor          = SarcGreen,
                errorBorderColor     = MaterialTheme.colorScheme.error
            )
        )
        error?.let {
            Text(text = it, fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
        }
    }
}

// ── Item de administrador ─────────────────────────────────────────────────────
@Composable
private fun AdminItem(
    admin: Administrador,
    onToggleActivo: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE7F6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint     = Color(0xFF7B1FA2),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = admin.nombreCompleto,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextDark
                )
                Text(
                    text     = admin.email,
                    fontSize = 12.sp,
                    color    = TextMid
                )
            }

            // Badge activo/inactivo
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (admin.activo) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
            ) {
                Text(
                    text     = if (admin.activo) "Activo" else "Inactivo",
                    fontSize = 11.sp,
                    color    = if (admin.activo) Color(0xFF388E3C) else Color(0xFFF57C00),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Toggle visibilidad (activo/inactivo)
            IconButton(
                onClick  = onToggleActivo,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.VisibilityOff,
                    contentDescription = if (admin.activo) "Desactivar" else "Activar",
                    tint     = TextLight,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Eliminar
            IconButton(
                onClick  = onEliminar,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint     = Color(0xFFD32F2F),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ── Lottie loading — usa cargandoVerde.json desde raw/ ───────────────────────
@Composable
private fun LottieLoading() {
    AndroidView(
        factory = { context ->
            LottieAnimationView(context).apply {
                setAnimation(R.raw.cargando_verde)
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
        },
        modifier = Modifier.size(60.dp)
    )
}

// ── Previews ──────────────────────────────────────────────────────────────────
private val adminsFake = listOf(
    Administrador(1, "Administrador Municipal", "admin@sarc.com", activo = true),
    Administrador(2, "Carlos García López",      "carlos@sarc.com", activo = false)
)

@Preview(showBackground = true, showSystemUi = true, name = "Lista con admins")
@Composable
private fun PreviewLista() {
    SarcTheme {
        CrearUsuarioContent(
            state = CrearUsuarioUiState(administradores = adminsFake)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Con formulario abierto")
@Composable
private fun PreviewFormulario() {
    SarcTheme {
        CrearUsuarioContent(
            state = CrearUsuarioUiState(
                mostrarFormulario = true,
                administradores   = adminsFake
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Cargando lista")
@Composable
private fun PreviewCargando() {
    SarcTheme {
        CrearUsuarioContent(
            state = CrearUsuarioUiState(isLoadingLista = true)
        )
    }
}

// Composable interno para preview sin ViewModel
@Composable
internal fun CrearUsuarioContent(
    state: CrearUsuarioUiState,
    onBack: () -> Unit = {},
    onCrearClick: () -> Unit = {},
    onNombreChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onContrasenaChange: (String) -> Unit = {},
    onCrear: () -> Unit = {},
    onCancelar: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(SarcGreen)) {
        CrearUsuarioTopBar(
            onBack            = onBack,
            onCrearClick      = onCrearClick,
            mostrarFormulario = state.mostrarFormulario
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F0FF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(visible = state.mostrarFormulario, enter = expandVertically(), exit = shrinkVertically()) {
                FormularioAdminCard(
                    state              = state,
                    onNombreChange     = onNombreChange,
                    onEmailChange      = onEmailChange,
                    onContrasenaChange = onContrasenaChange,
                    onCrear            = onCrear,
                    onCancelar         = onCancelar
                )
            }
            Card(
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier  = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Administradores del Sistema", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                    if (state.isLoadingLista) {
                        Box(Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = SarcGreen, modifier = Modifier.size(32.dp))
                        }
                    } else {
                        state.administradores.forEach { admin ->
                            AdminItem(admin = admin, onToggleActivo = {}, onEliminar = {})
                        }
                    }
                }
            }
        }
    }
}
