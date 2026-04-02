package com.example.smsledger.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB), // blue-600
    secondary = Color(0xFF3B82F6), // blue-500
    tertiary = Color(0xFF60A5FA), // blue-400
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFFEFF6FF), // blue-50
    onPrimaryContainer = Color(0xFF1E40AF), // blue-800
    secondaryContainer = Color(0xFFF1F5F9), // slate-100
    onSecondaryContainer = Color(0xFF475569), // slate-600
    surfaceVariant = Color(0xFFF8FAFC), // slate-50
    onSurfaceVariant = Color(0xFF64748B), // slate-500
    outline = Color(0xFFE2E8F0), // slate-200
)

@Composable
fun SmsLedgerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
