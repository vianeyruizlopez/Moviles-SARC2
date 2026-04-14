package com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.williamsel.sarc.features.ciudadano.crearreportes.domain.entities.CategoriaIncidencia
import com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.screens.CrearReportesUiState
import com.williamsel.sarc.features.ciudadano.crearreportes.presentacion.viewmodels.CrearReportesViewModel
import com.williamsel.sarc.ui.theme.SarcGreen
import com.williamsel.sarc.ui.theme.SarcTheme
import com.williamsel.sarc.ui.theme.SurfaceWhite
import com.williamsel.sarc.ui.theme.TextDark
import com.williamsel.sarc.ui.theme.TextLight
import com.williamsel.sarc.ui.theme.TextMid
import com.williamsel.sarc.ui.theme.WarningAmber

@Composable
fun CrearReporteScreen(
    idUsuario: Int = 1,
    onReporteCreado: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: CrearReportesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isEnviado) {
        if (state.isEnviado) {
            viewModel.resetEnviado()
            onReporteCreado()
        }
    }

    state.errorMessage?.let { LaunchedEffect(it) { viewModel.clearError() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SarcGreen)
    ) {
        CrearReporteTopBar(onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceWhite)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text     = "Reporta una incidencia en tu comunidad",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color    = TextDark
            )
            Text(
                text     = "Completa los campos requeridos para enviar tu reporte",
                fontSize = 12.sp,
                color    = TextMid
            )

            CategoriaSection(
                categorias            = state.categorias,
                categoriaSeleccionada = state.categoriaSeleccionada,
                onCategoriaClick      = viewModel::onCategoriaSeleccionada,
                error                 = state.errorCategoria
            )

            CampoTexto(
                label       = "Título del reporte *",
                value       = state.titulo,
                onValueChange = viewModel::onTituloChange,
                placeholder = "Ej. Bache grande en avenida principal",
                error       = state.errorTitulo
            )

            CampoTexto(
                label         = "Descripción del problema *",
                value         = state.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                placeholder   = "Describe detalladamente el problema que estás reportando...",
                maxLines      = 4,
                minLines      = 4,
                error         = state.errorDescripcion
            )

            FotografiaSection(
                imagen              = state.imagen,
                onImagenSeleccionada = viewModel::onImagenSeleccionada,
                onImagenEliminada   = viewModel::onImagenEliminada
            )

            UbicacionSection(
                ubicacion     = state.ubicacion,
                error         = state.errorUbicacion,
                onGpsClick    = viewModel::obtenerUbicacion
            )

            state.errorMessage?.let { msg ->
                Text(
                    text     = msg,
                    fontSize = 13.sp,
                    color    = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = onBack,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextMid),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDDDDD))
                ) {
                    Text("Cancelar", fontSize = 14.sp)
                }

                Button(
                    onClick  = { viewModel.enviarReporte(idUsuario) },
                    enabled  = !state.isLoading,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = SarcGreen)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color    = SurfaceWhite,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Enviar Reporte", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CrearReporteTopBar(onBack: () -> Unit) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint               = SurfaceWhite
            )
        }
        Text(
            text       = "Crear Reporte",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = SurfaceWhite
        )
    }
}

@Composable
private fun CategoriaSection(
    categorias: List<CategoriaIncidencia>,
    categoriaSeleccionada: CategoriaIncidencia?,
    onCategoriaClick: (CategoriaIncidencia) -> Unit,
    error: String?
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LabelCampo(texto = "Categoría de la incidencia *")

        val filas = categorias.chunked(2)
        filas.forEach { fila ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                fila.forEach { categoria ->
                    val seleccionada = categoriaSeleccionada?.id == categoria.id
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp)
                            .clickable { onCategoriaClick(categoria) }
                            .then(
                                if (seleccionada)
                                    Modifier.border(2.dp, SarcGreen, RoundedCornerShape(12.dp))
                                else
                                    Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                            ),
                        shape  = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionada) Color(0xFFE8F5E9) else SurfaceWhite
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (seleccionada) 0.dp else 1.dp
                        )
                    ) {
                        Column(
                            modifier             = Modifier.fillMaxSize(),
                            horizontalAlignment  = Alignment.CenterHorizontally,
                            verticalArrangement  = Arrangement.Center
                        ) {
                            Text(text = categoria.emoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text       = categoria.nombre,
                                fontSize   = 12.sp,
                                fontWeight = if (seleccionada) FontWeight.SemiBold else FontWeight.Normal,
                                color      = if (seleccionada) SarcGreen else TextDark
                            )
                        }
                    }
                }
                if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }

        error?.let {
            Text(text = it, fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLines: Int = 1,
    minLines: Int = 1,
    error: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LabelCampo(texto = label)
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = TextLight, fontSize = 13.sp) },
            maxLines      = maxLines,
            minLines      = minLines,
            shape         = RoundedCornerShape(12.dp),
            modifier      = Modifier.fillMaxWidth(),
            isError       = error != null,
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = SarcGreen,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedTextColor     = TextDark,
                unfocusedTextColor   = TextDark,
                cursorColor          = SarcGreen,
                errorBorderColor     = MaterialTheme.colorScheme.error
            )
        )
        error?.let {
            Text(text = it, fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun FotografiaSection(
    imagen: Bitmap?,
    onImagenSeleccionada: (Bitmap) -> Unit,
    onImagenEliminada: () -> Unit
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { onImagenSeleccionada(it) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LabelCampo(texto = "Fotografía")

        if (imagen != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    bitmap             = imagen.asImageBitmap(),
                    contentDescription = "Fotografía del reporte",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
            }
            Button(
                onClick  = { cameraLauncher.launch() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = WarningAmber)
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Cambiar foto",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = SurfaceWhite
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    .clickable { cameraLauncher.launch() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector        = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint               = Color(0xFFBDBDBD),
                        modifier           = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text     = "Toca para agregar foto",
                        fontSize = 13.sp,
                        color    = TextLight
                    )
                }
            }
        }
    }
}

@Composable
private fun UbicacionSection(
    ubicacion: String,
    error: String?,
    onGpsClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LabelCampo(texto = "Ubicación")

        OutlinedTextField(
            value         = ubicacion,
            onValueChange = {},
            readOnly      = true,
            placeholder   = {
                Text(
                    "Av. Principal #123, Suchiapa, Chi...",
                    color    = TextLight,
                    fontSize = 13.sp
                )
            },
            trailingIcon  = {
                IconButton(onClick = onGpsClick) {
                    Icon(
                        imageVector        = Icons.Default.LocationOn,
                        contentDescription = "Obtener ubicación GPS",
                        tint               = SarcGreen
                    )
                }
            },
            shape    = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            isError  = error != null,
            colors   = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = SarcGreen,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedTextColor     = TextDark,
                unfocusedTextColor   = TextDark,
                disabledTextColor    = TextDark,
                errorBorderColor     = MaterialTheme.colorScheme.error
            )
        )

        Text(
            text     = if (error != null) error
                       else "Ubicación detectada automáticamente por GPS",
            fontSize = 11.sp,
            color    = if (error != null) MaterialTheme.colorScheme.error else TextLight
        )
    }
}

@Composable
private fun LabelCampo(texto: String) {
    Text(
        text       = texto,
        fontSize   = 13.sp,
        fontWeight = FontWeight.Medium,
        color      = TextDark
    )
}

private val categoriasFake = listOf(
    CategoriaIncidencia(1, "Bache",     "🚧"),
    CategoriaIncidencia(2, "Basura",    "🗑️"),
    CategoriaIncidencia(3, "Alumbrado", "💡"),
    CategoriaIncidencia(4, "Otro",      "📋")
)

@Preview(showBackground = true, showSystemUi = true, name = "Sin imagen")
@Composable
private fun PreviewSinImagen() {
    SarcTheme {
        CrearReporteScreen()
    }
}
