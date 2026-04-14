package com.williamsel.sarc.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
private val LightColorScheme = lightColorScheme(
    primary          = SarcGreen,
    onPrimary        = SurfaceWhite,
    primaryContainer = SarcGreenLight,
    secondary        = SarcGreenDark,
    onSecondary      = SurfaceWhite,
    background       = BackgroundLight,
    onBackground     = TextDark,
    surface          = SurfaceWhite,
    onSurface        = TextDark,
    surfaceVariant   = FieldBackground,
    onSurfaceVariant = TextMid,
    error            = ErrorRed,
    onError          = SurfaceWhite
)

private val DarkColorScheme = darkColorScheme(
    primary          = SarcGreen,
    onPrimary        = DarkNavy,
    primaryContainer = SarcGreenDark,
    secondary        = SarcGreenLight,
    onSecondary      = DarkNavy,
    background       = DarkNavy,
    onBackground     = SurfaceWhite,
    surface          = DarkNavy,
    onSurface        = SurfaceWhite,
    surfaceVariant   = TextMid,
    onSurfaceVariant = SurfaceWhite,
    error            = ErrorRed,
    onError          = SurfaceWhite
)

@Composable
fun SarcTheme(
    darkTheme: Boolean    = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
