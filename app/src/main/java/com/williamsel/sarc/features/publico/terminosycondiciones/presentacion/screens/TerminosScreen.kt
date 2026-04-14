package com.williamsel.sarc.features.publico.terminosycondiciones.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.williamsel.sarc.ui.theme.SarcTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Términos y Condiciones",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Última actualización: 20 de marzo, 2026",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Volver", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Acepto los Términos y Condiciones", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "© 2026 Municipio de Suchiapa, Chiapas. Todos los derechos reservados.",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WelcomeCard()

            SectionCard(number = "1.", title = "Aceptación de Términos", icon = Icons.Default.CheckCircle) {
                SectionParagraph(
                    "Al acceder y utilizar el Sistema de Atención y Reporte Ciudadano (SARC), " +
                            "usted acepta estar sujeto a estos Términos y Condiciones. Si no está de " +
                            "acuerdo con alguna parte de estos términos, no debe utilizar este servicio."
                )
            }

            SectionCard(number = "2.", title = "Uso del Servicio", icon = Icons.Default.Build) {
                SectionParagraph(
                    "SARC es un sistema diseñado para facilitar la comunicación entre " +
                            "ciudadanos y el Municipio de Suchiapa, Chiapas, permitiendo reportar " +
                            "incidencias urbanas como baches, basura, alumbrado público y otros " +
                            "problemas municipales."
                )
                Spacer(modifier = Modifier.height(8.dp))
                CheckItem("El servicio es gratuito para todos los ciudadanos")
                CheckItem("Se requiere registro para crear reportes")
                CheckItem("Los reportes deben ser veraces y relacionados con problemas municipales")
                CheckItem("No se permite el uso abusivo o fraudulento del sistema")
            }

            SectionCard(number = "3.", title = "Responsabilidades del Usuario", icon = Icons.Default.Person) {
                SectionParagraph("Al utilizar SARC, usted se compromete a:")
                Spacer(modifier = Modifier.height(8.dp))
                CheckItem("Proporcionar información veraz y precisa en sus reportes")
                CheckItem("No realizar reportes falsos o maliciosos")
                CheckItem("Respetar las leyes municipales, estatales y federales")
                CheckItem("Mantener la confidencialidad de sus credenciales de acceso")
                CheckItem("No compartir contenido ofensivo, difamatorio o inapropiado")
                CheckItem("Utilizar el sistema únicamente para reportar problemas municipales legítimos")
            }

            SectionCard(number = "4.", title = "Contenido Generado por Usuarios", icon = Icons.Default.Create) {
                SectionParagraph("Todo contenido enviado a través de SARC (fotos, descripciones, ubicaciones):")
                Spacer(modifier = Modifier.height(8.dp))
                CheckItem("Puede ser revisado por personal municipal")
                CheckItem("Será utilizado exclusivamente para atender y resolver las incidencias reportadas")
                CheckItem("No será compartido con terceros sin autorización previa")
                CheckItem("Debe respetar los derechos de terceros y la propiedad intelectual")
                CheckItem("No debe contener información personal sensible de terceros")
            }

            SectionCard(number = "5.", title = "Privacidad y Datos Personales", icon = Icons.Default.Lock) {
                SectionParagraph(
                    "El Municipio de Suchiapa se compromete a proteger su información " +
                            "personal conforme a la Ley Federal de Protección de Datos Personales en " +
                            "Posesión de los Particulares. Para más información, consulte nuestras " +
                            "Políticas de Privacidad."
                )
            }

            SectionCard(number = "6.", title = "Limitación de Responsabilidad", icon = Icons.Default.Warning) {
                SectionParagraph("El Municipio de Suchiapa:")
                Spacer(modifier = Modifier.height(8.dp))
                CheckItem("No garantiza tiempos específicos de resolución de reportes")
                CheckItem("Se reserva el derecho de priorizar reportes según gravedad y recursos disponibles")
                CheckItem("No se hace responsable por daños derivados del uso indebido del sistema")
                CheckItem("Puede suspender o eliminar cuentas que violen estos términos")
                CheckItem("No garantiza disponibilidad ininterrumpida del servicio")
            }

            SectionCard(number = "7.", title = "Modificaciones del Servicio", icon = Icons.Default.Refresh) {
                SectionParagraph(
                    "El Municipio se reserva el derecho de modificar, suspender o " +
                            "descontinuar el servicio SARC en cualquier momento, con o sin previo aviso. " +
                            "Las modificaciones a estos términos serán notificadas a través de la plataforma."
                )
            }

            SectionCard(number = "8.", title = "Propiedad Intelectual", icon = Icons.Default.Star) {
                SectionParagraph(
                    "SARC, su logotipo, diseño y funcionalidades son propiedad del " +
                            "Municipio de Suchiapa, Chiapas. Queda prohibida la reproducción, " +
                            "distribución o modificación sin autorización expresa."
                )
            }

            SectionCard(number = "9.", title = "Jurisdicción", icon = Icons.Default.LocationOn) {
                SectionParagraph(
                    "Estos términos se rigen por las leyes de los Estados Unidos Mexicanos. " +
                            "Cualquier controversia será resuelta en los tribunales competentes " +
                            "del Estado de Chiapas."
                )
            }

            SectionCard(number = "10.", title = "Contacto", icon = Icons.Default.Email) {
                SectionParagraph("Para preguntas sobre estos Términos y Condiciones, contáctenos a:")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "contacto@municipiosuchiapa.gob.mx",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = "Bienvenido a SARC",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Estos Términos y Condiciones establecen las reglas y regulaciones " +
                            "para el uso del Sistema de Atención y Reporte Ciudadano (SARC) del " +
                            "Municipio de Suchiapa, Chiapas. Por favor, léalos cuidadosamente " +
                            "antes de utilizar nuestro servicio.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    number: String,
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "$number $title",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SectionParagraph(text: String) {
    Text(text = text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 19.sp)
}

@Composable
private fun CheckItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(12.dp)
            )
        }
        Text(
            text = text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 19.sp, modifier = Modifier.weight(1f)
        )
    }
}
