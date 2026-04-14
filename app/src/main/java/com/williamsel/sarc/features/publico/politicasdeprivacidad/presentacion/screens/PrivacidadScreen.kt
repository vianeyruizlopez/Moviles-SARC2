package com.williamsel.sarc.features.publico.politicasdeprivacidad.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
fun PrivacidadScreen(
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Políticas de Privacidad",
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
                    Text(
                        text = "Volver",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Acepto las Políticas de Privacidad",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
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

            SectionCard(number = "1.", title = "Información que Recopilamos", icon = Icons.Default.Info) {
                SectionParagraph("SARC recopila información necesaria para proporcionar un servicio eficiente de atención ciudadana:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Información de registro: nombre completo, correo electrónico")
                BulletItem("Información de los reportes: ubicación GPS, fotografías, descripciones de incidencias")
                BulletItem("Datos técnicos: dirección IP, tipo de dispositivo")
                BulletItem("Interacciones de uso: interacciones con la plataforma, historial de reportes")
            }

            SectionCard(number = "2.", title = "Cómo Utilizamos su Información", icon = Icons.Default.Settings) {
                SectionParagraph("Los datos recopilados se utilizan exclusivamente para:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Procesar y atender los reportes ciudadanos")
                BulletItem("Mejorar los servicios")
                BulletItem("Comunicarnos con usted sobre el estado de sus incidencias")
                BulletItem("Generar estadísticas y análisis de incidencias")
                BulletItem("Verificar la autenticidad de los reportes")
                BulletItem("Cumplir con obligaciones legales y regulatorias")
            }

            SectionCard(number = "3.", title = "Protección de Datos", icon = Icons.Default.Lock) {
                SectionParagraph("El Municipio de Suchiapa implementa medidas de seguridad para proteger su información:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Cifrado de datos en tránsito y almacenamiento")
                BulletItem("Controles de acceso restringidos al personal autorizado")
                BulletItem("Auditorías de seguridad periódicas")
                BulletItem("Respaldo regular de información")
                BulletItem("Protocolos de respuesta ante incidentes de seguridad")
            }

            SectionCard(number = "4.", title = "Compartición de Información", icon = Icons.Default.Share) {
                SectionParagraph("Su información NO será compartida con terceros, excepto en los siguientes casos:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Con autoridades competentes cuando sea requerido legalmente")
                BulletItem("Con personal municipal responsable de atender las incidencias")
                BulletItem("Con su consentimiento explícito")
                BulletItem("Información agregada y anónima para estadísticas públicas")
            }

            SectionCard(number = "5.", title = "Sus Derechos ARCO", icon = Icons.Default.Person) {
                SectionParagraph("De conformidad con la Ley Federal de Protección de Datos Personales, usted tiene derecho a:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Acceder a sus datos personales en nuestra posesión")
                BulletItem("Rectificar datos incorrectos o incompletos")
                BulletItem("Cancelar su cuenta y datos asociados")
                BulletItem("Oponerse al uso de sus datos para fines específicos")
                BulletItem("Revocar el consentimiento otorgado")
                BulletItem("Limitar el uso o divulgación de sus datos")
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Nota: Para ejercer estos derechos, contáctanos a: privacidad@municipiosuchiapa.gob.mx",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 18.sp
                    )
                }
            }

            SectionCard(number = "6.", title = "Fotografías y Ubicación", icon = Icons.Default.LocationOn) {
                SectionParagraph("Aspectos importantes sobre fotos y ubicación GPS:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Las fotografías se utilizan exclusivamente para documentar incidencias")
                BulletItem("No compartimos rostros identificables de personas sin su consentimiento")
                BulletItem("La ubicación GPS se utiliza para geolocalizar la incidencia reportada")
                BulletItem("Puede desactivar el GPS de su dispositivo, pero deberá ingresar la ubicación manualmente")
                BulletItem("Las fotos pueden ser revisadas por personal municipal autorizado")
            }

            SectionCard(number = "7.", title = "Retención de Datos", icon = Icons.Default.DateRange) {
                SectionParagraph("Conservamos su información durante:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Reportes activos: mientras dure el proceso de atención")
                BulletItem("Reportes resueltos: hasta 5 años para fines estadísticos y de auditoría")
                BulletItem("Datos de cuenta: mientras mantenga su cuenta")
                BulletItem("Cuentas inactivas: se pueden eliminar después de 2 años sin actividad")
                BulletItem("Puede solicitar la eliminación de sus datos en cualquier momento")
            }

            SectionCard(number = "8.", title = "Cookies y Tecnologías Similares", icon = Icons.Default.Notifications) {
                SectionParagraph("SARC utiliza cookies y tecnologías similares para:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Mantener su sesión activa de forma segura")
                BulletItem("Recordar sus preferencias de usuario")
                BulletItem("Analizar el uso de la plataforma para mejorarla")
                BulletItem("Puede configurar su navegador para rechazar cookies, aunque esto puede afectar la funcionalidad")
            }

            SectionCard(number = "9.", title = "Menores de Edad", icon = Icons.Default.Face) {
                SectionParagraph(
                    "SARC está diseñada para usuarios mayores de 18 años. No recopilamos " +
                            "información de menores sin el consentimiento de sus padres o tutores legales."
                )
            }

            SectionCard(number = "10.", title = "Cambios a esta Política", icon = Icons.Default.Edit) {
                SectionParagraph(
                    "El Municipio de Suchiapa se reserva el derecho de modificar esta " +
                            "Política de Privacidad. Los cambios serán notificados a través de " +
                            "la plataforma y por correo electrónico. El uso continuado del servicio " +
                            "después de los cambios constituye su aceptación de la nueva política."
                )
            }

            SectionCard(number = "11.", title = "Contacto", icon = Icons.Default.Email) {
                SectionParagraph("Para preguntas, comentarios o solicitudes relacionadas con esta Política de Privacidad:")
                Spacer(modifier = Modifier.height(8.dp))
                BulletItem("Correo electrónico: privacidad@municipiosuchiapa.gob.mx")
                BulletItem("Teléfono: +52 961 XXX XXXX")
                BulletItem("Dirección: Palacio Municipal, Suchiapa, Chiapas, México")
                BulletItem("Horario de atención: Lunes a Viernes, 8:00 AM - 4:00 PM")
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Aviso Importante",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Esta Política de Privacidad se rige por la Ley Federal de " +
                                    "Protección de Datos Personales en Posesión de los Particulares, " +
                                    "así como por las disposiciones aplicables en materia de protección " +
                                    "de datos personales en el Estado de Chiapas.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            lineHeight = 18.sp
                        )
                    }
                }
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
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = "Su Privacidad es Importante",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "El Municipio de Suchiapa, Chiapas, está comprometido con la " +
                            "protección de su privacidad y la seguridad de sus datos personales. " +
                            "Esta Política de Privacidad explica cómo recopilamos, usamos, " +
                            "protegemos y compartimos su información cuando utiliza el Sistema " +
                            "de Atención y Reporte Ciudadano (SARC).",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 19.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Al utilizar SARC, usted acepta las prácticas descritas en esta política.",
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
private fun BulletItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "•", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 1.dp)
        )
        Text(
            text = text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 19.sp, modifier = Modifier.weight(1f)
        )
    }
}
