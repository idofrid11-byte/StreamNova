package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NovaPrimary,
    onPrimary = Color.White,
    primaryContainer = NovaSurfaceElevated,
    onPrimaryContainer = NovaPrimaryBright,
    secondary = NovaSecondary,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0E3846),
    onSecondaryContainer = NovaAccentCyan,
    tertiary = NovaAccentPink,
    background = NovaDeepNavy,
    onBackground = NovaTextPrimary,
    surface = NovaSurface,
    onSurface = NovaTextPrimary,
    surfaceVariant = NovaSurfaceElevated,
    onSurfaceVariant = NovaTextSecondary,
    outline = NovaSurfaceBorder
)

@Composable
fun StreamNovaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
