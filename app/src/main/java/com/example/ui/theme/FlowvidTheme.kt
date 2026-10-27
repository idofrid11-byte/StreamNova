package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Flowvid Palette
val FlowvidNeonCyan = Color(0xFF00E5FF)       // Primary Accent
val FlowvidElectricPurple = Color(0xFF7C4DFF) // Secondary Accent
val FlowvidDeepOled = Color(0xFF0A0A0C)       // Deep OLED Background
val FlowvidGlassSurface = Color(0xFF141419)   // Glass Card Surface
val FlowvidGlassBorder = Color(0x3300E5FF)    // Subtle Cyan Border
val FlowvidGlassCardBg = Color(0xCC141419)    // Translucent Card Background
val FlowvidHotPink = Color(0xFFFF2A85)        // Action Pink
val FlowvidGold = Color(0xFFFFD700)           // Rating Gold
val FlowvidEmerald = Color(0xFF00E676)        // Seeders / Cached Green

val FlowvidDarkColorScheme = darkColorScheme(
    primary = FlowvidNeonCyan,
    secondary = FlowvidElectricPurple,
    background = FlowvidDeepOled,
    surface = FlowvidGlassSurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1C1C24),
    onSurfaceVariant = Color(0xFFB0B5C9),
    outline = FlowvidGlassBorder
)

@Composable
fun FlowvidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FlowvidDarkColorScheme,
        typography = Typography,
        content = content
    )
}
