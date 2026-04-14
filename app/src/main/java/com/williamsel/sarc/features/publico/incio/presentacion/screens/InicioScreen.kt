package com.williamsel.sarc.features.publico.incio.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.williamsel.sarc.ui.theme.SarcTheme

@Composable
fun InicioScreen(
    onIniciarSesion: () -> Unit = {},
    onCrearCuenta: () -> Unit = {},
    onTerminos: () -> Unit = {},
    onPrivacidad: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(top = 72.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        RoundedCornerShape(22.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "S",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SARC",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sistema de Atención a\nReportes Ciudadanos",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            FeatureCard(
                icon = Icons.Default.CameraAlt,
                title = "Reporta fácilmente",
                description = "Toma una foto y envía tu reporte en segundos"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureCard(
                icon = Icons.Default.LocationOn,
                title = "Ubicación precisa",
                description = "GPS automático para identificar exactamente el problema"
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onIniciarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCrearCuenta,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Crear Cuenta Nueva",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Municipio de Suchiapa, Chiapas 2026",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onTerminos, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = "Términos y\nCondiciones",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        lineHeight = 17.sp
                    )
                }

                Text(
                    text = "•",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                )

                TextButton(onClick = onPrivacidad, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = "Políticas\nde Privacidad",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun InicioScreenPreview() {
    SarcTheme { InicioScreen() }
}
