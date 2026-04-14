# 📍 SARC – Sistema de Atención a Reportes Ciudadanos

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-087CFA?style=for-the-badge&logo=ktor&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Estado](https://img.shields.io/badge/Estado-En_Desarrollo-yellow?style=for-the-badge)

> App móvil que permite a los ciudadanos reportar incidencias urbanas (baches, alumbrado, basura) con foto y ubicación GPS. Los administradores gestionan y dan seguimiento a los reportes mediante un mapa interactivo.

---

## Tabla de contenidos

- [Descripción](#-descripción)
- [Características](#-características)
- [Roles del sistema](#-roles-del-sistema)
- [Módulos](#-módulos)
- [Stack tecnológico](#-stack-tecnológico)
- [Instalación](#-instalación)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Capturas de pantalla](#-capturas-de-pantalla)
- [Autores](#-autores)
- [Licencia](#-licencia)

---

## Descripción

**SARC** es una aplicación móvil desarrollada para mejorar la comunicación entre ciudadanos y autoridades municipales. Permite reportar problemas urbanos de forma rápida y geolocalizada, facilitando su gestión y seguimiento por parte de los administradores responsables.

La aplicación busca contribuir a una gestión urbana más eficiente, transparente y participativa, eliminando los canales informales de reporte y centralizando la información en una sola plataforma.

---

## Características

- 📷 Reporte de incidencias con fotografía y descripción
- 📍 Registro automático de ubicación GPS al crear un reporte
- 🗺️ Visualización de reportes en mapa interactivo con filtros
- 🔄 Sistema de estados: **Pendiente → En Proceso → Resuelto**
- 👥 Roles diferenciados: Ciudadano, Administrador y Super Administrador
- 🔔 Notificaciones cuando el estado de un reporte cambia
- 📊 Historial de reportes con seguimiento en tiempo real

---

## Roles del sistema

| Rol | Descripción |
|-----|-------------|
| **Ciudadano** | Crea reportes de incidencias, consulta su historial y recibe notificaciones de estado |
| **Administrador** | Gestiona, clasifica y actualiza el estado de los reportes desde el panel de control |


---

## Módulos

### 1. Módulo de Autenticación
Registro e inicio de sesión con selección de rol.

### 2. Módulo de Registro de Reportes
Creación de nuevas incidencias con foto, descripción, categoría y geolocalización GPS.

### 3. Módulo de Gestión de Reportes
Panel del administrador para revisar, actualizar y gestionar todas las incidencias recibidas.

### 4. Módulo de Visualización Geográfica
Mapa interactivo con marcadores por categoría y estado, con navegación al punto del reporte.

### 5. Módulo de Historial y Seguimiento
Consulta del estado actual e historial completo de reportes por ciudadano o administrador.

---

## Stack tecnológico

| Capa | Tecnología |
|------|-----------|
| **Lenguaje** | Kotlin |
| **UI** | Jetpack Compose |
| **Backend / API** | Ktor |
| **Mapas** | Google Maps SDK |
| **Autenticación** | JWT |
| **Base de datos** | PostgreSQL |
| **Almacenamiento** | SqlLite |

---

## Instalación

```bash
# Clonar el repositorio
git clone https://github.com/Williams-Espinosa/SARC.git

# Entrar al directorio
cd SARC

# Abrir el proyecto en Android Studio
# File > Open > seleccionar la carpeta del proyecto

# Sincronizar dependencias con Gradle
./gradlew build
```

> **Requisitos:** Android Studio Hedgehog o superior · JDK 17 · Android SDK 26+

---

## Estructura del proyecto

```
SARC/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/sarc/
│   │   │   │   ├── ui/          # Pantallas y componentes Compose
│   │   │   │   ├── data/        # Repositorios y fuentes de datos
│   │   │   │   ├── domain/      # Casos de uso y modelos
│   │   │   │   └── di/          # Inyección de dependencias
│   │   │   └── res/             # Recursos (íconos, strings, temas)
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── routes/          # Endpoints de la API Ktor
│   │       ├── models/          # Modelos de datos
│   │       └── plugins/         # Configuración de Ktor
├── README.md
└── build.gradle.kts
```

---

## Capturas de pantalla

> *Próximamente...*

---

## Autores

| Nombre | Rol |
|--------|-----|
| Williams Espinosa | Desarrollo UI/UX & Android |
| Vianey Ruiz | Desarrollo Backend |
> 📍 Universidad Politécnica de Chiapas · Ingeniería en Tecnologia de la información e innovación digital · 2026

---

## 📄 Licencia
![Licencia: Privada](https://img.shields.io/badge/Licencia-No_Comercial-red?style=for-the-badge)

---

## Propiedad Intelectual
Todo el contenido de este repositorio, incluyendo el código fuente, diseño gráfico, está protegido por las leyes de propiedad intelectual.
* **Uso permitido:** Consulta, aprendizaje y exhibición personal.
* **Prohibiciones:** Se prohíbe estrictamente la copia parcial o total para uso comercial, la reventa del software.

> ###  *"Crea el presente, codifica el futuro."*
> © 2026 **Williams-Espinosa**. Todos los derechos reservados.

---