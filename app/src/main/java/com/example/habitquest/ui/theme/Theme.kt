package com.example.habitquest.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = HabitQuestPrimary,
    onPrimary = HabitQuestOnPrimary,
    primaryContainer = HabitQuestSecondary,
    onPrimaryContainer = HabitQuestOnBackground,
    secondary = HabitQuestSecondary,
    onSecondary = HabitQuestOnBackground,
    secondaryContainer = HabitQuestTertiary,
    onSecondaryContainer = HabitQuestOnBackground,
    tertiary = HabitQuestTertiary,
    onTertiary = HabitQuestOnBackground,
    background = HabitQuestBackground,
    onBackground = HabitQuestOnBackground,
    surface = HabitQuestSurface,
    onSurface = HabitQuestOnSurface,
    surfaceVariant = HabitQuestSurface,
    onSurfaceVariant = HabitQuestOnBackground,
    surfaceTint = HabitQuestPrimary,
    outline = HabitQuestPrimary.copy(alpha = 0.5f),
    outlineVariant = HabitQuestSecondary,
    error = Color(0xFFFF3333),
    onError = Color.White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = HabitQuestPrimary,
    onPrimary = HabitQuestOnPrimary,
    primaryContainer = HabitQuestSecondary,
    onPrimaryContainer = HabitQuestOnBackground,
    secondary = HabitQuestSecondary,
    onSecondary = HabitQuestOnBackground,
    secondaryContainer = HabitQuestTertiary,
    onSecondaryContainer = HabitQuestOnBackground,
    tertiary = HabitQuestTertiary,
    onTertiary = HabitQuestOnBackground,
    background = HabitQuestBackground,
    onBackground = HabitQuestOnBackground,
    surface = HabitQuestSurface,
    onSurface = HabitQuestOnSurface,
    surfaceVariant = HabitQuestSurface,
    onSurfaceVariant = HabitQuestOnBackground,
    surfaceTint = HabitQuestPrimary,
    outline = HabitQuestPrimary.copy(alpha = 0.5f),
    outlineVariant = HabitQuestSecondary,
    error = Color(0xFFFF3333),
    onError = Color.White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color.White
)

@Composable
fun HabitQuestTheme(
    darkTheme: Boolean = true, // Siempre usar tema oscuro por defecto
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Deshabilitar colores dinámicos para consistencia
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
            // Configurar colores de las barras del sistema
            window.statusBarColor = HabitQuestBackground.toArgb()
            window.navigationBarColor = HabitQuestBackground.toArgb()

            // Configurar apariencia de los íconos (oscuros sobre fondo oscuro)
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false  // Íconos claros en barra de estado
                isAppearanceLightNavigationBars = false  // Íconos claros en barra de navegación
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}