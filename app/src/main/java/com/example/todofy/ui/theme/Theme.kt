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

// Light Theme Colors - Skema warna modern dengan aksen yang lebih cerah
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),          // Ungu utama yang lebih cerah
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),  // Container ungu yang lembut
    onPrimaryContainer = Color(0xFF21005E),

    secondary = Color(0xFF03DAC6),        // Teal mencolok
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFCEFAF8), // Container teal yang lembut
    onSecondaryContainer = Color(0xFF00413F),

    tertiary = Color(0xFFFF8F00),         // Aksen oranye yang hangat
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFE0B2), // Container oranye yang lembut
    onTertiaryContainer = Color(0xFF502900),

    error = Color(0xFFFF3B30),            // Warna error yang lebih cerah
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD5),    // Container error yang lembut
    onErrorContainer = Color(0xFF410001),

    background = LightBackground,         // Menggunakan warna dari Color.kt
    onBackground = LightTextPrimary,      // Menggunakan warna dari Color.kt

    surface = LightSurface,               // Menggunakan warna dari Color.kt
    onSurface = LightTextPrimary,         // Menggunakan warna dari Color.kt

    surfaceVariant = LightSurfaceVariant, // Menggunakan warna dari Color.kt
    onSurfaceVariant = Color(0xFF4A4A4A), // Teks kontras lebih baik

    outline = Color(0xFF757575),          // Outline yang lebih jelas
    outlineVariant = Color(0xFFDCDCDC)    // Variant outline lebih lembut
)

// Dark Theme Colors - Kontras yang lebih baik dan warna yang lebih hidup
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),          // Ungu yang lebih cerah untuk dark theme
    onPrimary = Color.Black,              // Teks gelap pada warna terang
    primaryContainer = Color(0xFF4F378B),  // Container ungu yang lebih gelap
    onPrimaryContainer = Color(0xFFEADDFF), // Teks terang pada container gelap

    secondary = Color(0xFF03DAC6),        // Teal yang konsisten dengan light theme
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF00504F), // Container teal yang lebih gelap
    onSecondaryContainer = Color(0xFFCEFAF8), // Teks terang pada container gelap

    tertiary = Color(0xFFFFB74D),         // Oranye yang lebih cerah untuk dark theme
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF7F5000), // Container oranye yang lebih gelap
    onTertiaryContainer = Color(0xFFFFE0B2), // Teks terang pada container gelap

    error = Color(0xFFFF5252),            // Warna error yang lebih cerah
    onError = Color.Black,
    errorContainer = Color(0xFF8B0000),    // Container error yang lebih gelap
    onErrorContainer = Color(0xFFFFDAD5),  // Teks terang pada container gelap

    background = DarkBackground,          // Menggunakan warna dari Color.kt
    onBackground = DarkTextPrimary,       // Menggunakan warna dari Color.kt

    surface = DarkSurface,                // Menggunakan warna dari Color.kt
    onSurface = DarkTextPrimary,          // Menggunakan warna dari Color.kt

    surfaceVariant = DarkSurfaceVariant,  // Menggunakan warna dari Color.kt
    onSurfaceVariant = Color(0xFFDDDDDD), // Teks terang pada surface variant

    outline = Color(0xFF9E9E9E),          // Outline yang lebih jelas untuk dark theme
    outlineVariant = Color(0xFF555555)    // Variant outline lebih gelap
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
            window.statusBarColor = Color.Transparent.toArgb() // Transparent status bar untuk edge-to-edge

            // Setup edge-to-edge display
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Adjust status bar icons
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

            // Make bottom navigation bar transparent
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Menggunakan Typography dari Type.kt
        content = content
    )
}