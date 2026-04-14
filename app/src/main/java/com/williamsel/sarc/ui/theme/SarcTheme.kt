package com.williamsel.sarc.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SarcColorScheme = lightColorScheme(
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

@Composable
fun SarcTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SarcColorScheme,
        typography  = Typography,
        content     = content
    )
}