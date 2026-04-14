# Configuración del proyecto SARC

## Requisitos
- Android Studio Hedgehog o superior
- JDK 17
- Android SDK 35 (se descarga automático desde Android Studio)

## Pasos para compilar

1. Abrir el proyecto en Android Studio: File > Open > seleccionar la carpeta
2. Esperar a que Gradle sincronice las dependencias (puede tardar unos minutos la primera vez)
3. Si Android Studio no detecta el SDK, ir a File > Project Structure > SDK Location y configurar la ruta
4. Ejecutar la app en un emulador o dispositivo físico con API 26+

## Notas
- El archivo `local.properties` ya incluye la MAPS_API_KEY para Google Maps
- Android Studio generará automáticamente la ruta del SDK en `local.properties` al abrir el proyecto
- Si el emulador no tiene Google Play Services, el mapa y la autenticación con Google no funcionarán — usar un emulador con "Google APIs"
