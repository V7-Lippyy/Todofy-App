package com.example.todofy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),          // Blue primary
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),  // Light blue container
    onPrimaryContainer = Color(0xFF0A2744),

    secondary = Color(0xFF00796B),        // Teal secondary
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2DFDB), // Light teal container
    onSecondaryContainer = Color(0xFF002B25),

    tertiary = Color(0xFF7B1FA2),         // Purple tertiary
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE1BEE7), // Light purple container
    onTertiaryContainer = Color(0xFF3F0053),

    error = Color(0xFFE53935),            // Red error
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),    // Light red container
    onErrorContainer = Color(0xFF5F0000),

    background = LightBackground,         // Menggunakan warna dari Color.kt
    onBackground = LightTextPrimary,      // Menggunakan warna dari Color.kt

    surface = LightSurface,               // Menggunakan warna dari Color.kt
    onSurface = LightTextPrimary,         // Menggunakan warna dari Color.kt

    surfaceVariant = LightSurfaceVariant, // Menggunakan warna dari Color.kt
    onSurfaceVariant = Color(0xFF41484D), // Dark text on surface variant

    outline = Color(0xFF7C8589),          // Medium gray outline
    outlineVariant = Color(0xFFC5CACD)    // Light gray outline variant
)

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),          // Light blue primary for dark theme
    onPrimary = Color(0xFF0C355A),         // Dark blue on light blue
    primaryContainer = Color(0xFF0D47A1),  // Deep blue container
    onPrimaryContainer = Color(0xFFD1E4FF), // Light text on deep blue

    secondary = Color(0xFF80CBC4),        // Light teal secondary
    onSecondary = Color(0xFF00423B),       // Dark teal on light teal
    secondaryContainer = Color(0xFF00695C), // Deep teal container
    onSecondaryContainer = Color(0xFFB2DFDB), // Light text on deep teal

    tertiary = Color(0xFFCE93D8),         // Light purple tertiary
    onTertiary = Color(0xFF4A0072),        // Dark purple on light purple
    tertiaryContainer = Color(0xFF6A1B9A), // Deep purple container
    onTertiaryContainer = Color(0xFFE1BEE7), // Light text on deep purple

    error = Color(0xFFEF9A9A),            // Light red error
    onError = Color(0xFF7F0000),           // Dark red on light red
    errorContainer = Color(0xFFB71C1C),    // Deep red container
    onErrorContainer = Color(0xFFFFCDD2),  // Light text on deep red

    background = DarkBackground,          // Menggunakan warna dari Color.kt
    onBackground = DarkTextPrimary,       // Menggunakan warna dari Color.kt

    surface = DarkSurface,                // Menggunakan warna dari Color.kt
    onSurface = DarkTextPrimary,          // Menggunakan warna dari Color.kt

    surfaceVariant = DarkSurfaceVariant,  // Menggunakan warna dari Color.kt
    onSurfaceVariant = Color(0xFFBDBDBD), // Light text on dark surface variant

    outline = Color(0xFF9E9E9E),          // Medium gray outline for dark theme
    outlineVariant = Color(0xFF707070)    // Darker gray outline variant
)

@Composable
fun TodofyTheme(
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Update status bar color for more modern look
            window.statusBarColor = if (darkTheme) {
                Color(0xFF000000).toArgb() // Black status bar in dark mode
            } else {
                colorScheme.primary.toArgb() // Primary color in light mode
            }

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

            // Optional: make bottom navigation bar match theme
            window.navigationBarColor = if (darkTheme) {
                DarkBackground.toArgb() // Menggunakan warna dari Color.kt
            } else {
                LightBackground.toArgb() // Menggunakan warna dari Color.kt
            }
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Menggunakan Typography dari Type.kt
        content = content
    )
}