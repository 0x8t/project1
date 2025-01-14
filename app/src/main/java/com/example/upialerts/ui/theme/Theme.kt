package com.example.upialerts.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1a73e8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFe8f0fe),
    onPrimaryContainer = Color(0xFF1967d2),
    secondary = Color(0xFF1a73e8),
    secondaryContainer = Color(0xFFe8f0fe),
    surface = Color(0xFFffffff),
    onSurface = Color(0xFF202124),
    surfaceVariant = Color(0xFFf1f3f4),
    onSurfaceVariant = Color(0xFF5f6368),
    background = Color(0xFFf8f9fa),
    onBackground = Color(0xFF202124)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8ab4f8),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1f1f1f),
    onPrimaryContainer = Color(0xFF8ab4f8),
    secondary = Color(0xFF8ab4f8),
    secondaryContainer = Color(0xFF1f1f1f),
    surface = Color(0xFF202124),
    onSurface = Color(0xFFe8eaed),
    surfaceVariant = Color(0xFF3c4043),
    onSurfaceVariant = Color(0xFFe8eaed),
    background = Color(0xFF202124),
    onBackground = Color(0xFFe8eaed)
)

@Composable
fun UPIAlertsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}