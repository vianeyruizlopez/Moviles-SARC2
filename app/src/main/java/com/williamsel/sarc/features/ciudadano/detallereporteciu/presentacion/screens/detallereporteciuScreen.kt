package com.williamsel.sarc.features.ciudadano.detallereporteciu.presentacion.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.williamsel.sarc.features.ciudadano.detallereporteciu.domain.entities.Detallereporteciu
import com.williamsel.sarc.features.ciudadano.detallereporteciu.presentacion.viewmodels.DetallereporteciuViewModel
import com.williamsel.sarc.ui.theme.BlueProceso
import com.williamsel.sarc.ui.theme.GreenResuelto
import com.williamsel.sarc.ui.theme.OrangeWarning
import com.williamsel.sarc.ui.theme.PurpleAccent

@Composable
fun DetallereporteciuScreen(
    reporteId: String,
    onBack: () -> Unit,
    viewModel: DetallereporteciuViewModel = hiltViewModel()
) {
    LaunchedEffect(reporteId) { viewModel.cargar(reporteId) }
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = uiState) {
            is DetallereporteciuUIState.Loading -> ContentLoading()
            is DetallereporteciuUIState.Error   -> ContentError(state.mensaje, onBack)
            is DetallereporteciuUIState.Success -> ContentSuccess(state.reporte, onBack)
        }
    }
}

@Composable
private fun ContentSuccess(reporte: Detallereporteciu, onBack: () -> Unit) {
    val context    = LocalContext.current
    var visible    by remember { mutableStateOf(false) }
    val background = MaterialTheme.colorScheme.background
    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model              = reporte.imagenUrl,
                    contentDescription = "Imagen del reporte",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, background),
                                startY = 150f,
                                endY   = 520f
                            )
                        )
                )
                EstadoBadge(
                    estado   = reporte.estado,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 60.dp, end = 16.dp)
                )
            }

            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-20).dp)
                ) {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier          = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF0FDF4)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = reporte.iconoUrl ?: "❓",
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text          = "CATEGORÍA",
                                    style         = MaterialTheme.typography.labelSmall.copy(
                                        letterSpacing = 1.sp,
                                        fontWeight    = FontWeight.SemiBold
                                    ),
                                    color         = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text  = reporte.categoria,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text  = reporte.titulo,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 28.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.height(16.dp))

                    InfoCard(
                        icono      = Icons.Default.ChatBubbleOutline,
                        label      = "DESCRIPCIÓN",
                        valor      = reporte.descripcion,
                        colorIcono = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(10.dp))

                    InfoCard(
                        icono      = Icons.Default.LocationOn,
                        label      = "UBICACIÓN",
                        valor      = reporte.direccion,
                        colorIcono = PurpleAccent
                    )

                    Spacer(Modifier.height(10.dp))

                    InfoCard(
                        icono      = Icons.Default.CalendarMonth,
                        label      = "FECHA DE REPORTE",
                        valor      = reporte.fecha,
                        colorIcono = OrangeWarning
                    )

                    Spacer(Modifier.height(100.dp))
                }
            }
        }

        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick  = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.35f), CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint               = Color.White
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text  = "Detalle del Reporte",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
        }

        Button(
            onClick = {
                val uri = Uri.parse(
                    "geo:${reporte.latitud},${reporte.longitud}" +
                    "?q=${reporte.latitud},${reporte.longitud}"
                )
                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage("com.google.android.apps.maps")
                }
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .height(52.dp)
                .navigationBarsPadding(),
            shape  = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(Icons.Default.Navigation, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                text  = "Cómo llegar",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun InfoCard(
    icono: ImageVector,
    label: String,
    valor: String,
    colorIcono: Color
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                icono,
                contentDescription = label,
                tint               = colorIcono,
                modifier           = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight    = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    ),
                    color = colorIcono
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text     = valor,
                    style    = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color    = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun EstadoBadge(estado: String, modifier: Modifier = Modifier) {
    val color = when (estado.lowercase()) {
        "pendiente"  -> OrangeWarning
        "en proceso" -> BlueProceso
        "resuelto"   -> GreenResuelto
        else         -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        modifier  = modifier,
        shape     = RoundedCornerShape(20.dp),
        color     = color
    ) {
        Text(
            text  = estado.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color    = Color.White,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ContentLoading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ContentError(mensaje: String, onBack: () -> Unit) {
    Column(
        modifier            = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text  = "Error al cargar el reporte",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onBack,
            colors  = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Regresar")
        }
    }
}
