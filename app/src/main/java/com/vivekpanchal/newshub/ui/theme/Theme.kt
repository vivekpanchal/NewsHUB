package com.vivekpanchal.newshub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Role mapping: primary = Signal (breaking/live/urgent - the thing that says "now"),
// secondary = Wave (trending/viral/active-selection), tertiary = Gold (rank numbers, sparing use).
// error reuses Signal - both are "needs attention" in this app's vocabulary.
private val LightColors = lightColorScheme(
    primary = NewsHubColors.Signal,
    onPrimary = NewsHubColors.OnSignal,
    primaryContainer = NewsHubColors.Signal.copy(alpha = 0.12f),
    onPrimaryContainer = NewsHubColors.Signal,
    secondary = NewsHubColors.Wave,
    onSecondary = NewsHubColors.OnWave,
    secondaryContainer = NewsHubColors.Wave.copy(alpha = 0.16f),
    onSecondaryContainer = NewsHubColors.Ink,
    tertiary = NewsHubColors.Gold,
    onTertiary = NewsHubColors.OnGold,
    background = NewsHubColors.Paper,
    onBackground = NewsHubColors.Ink,
    surface = NewsHubColors.SurfaceLight,
    onSurface = NewsHubColors.Ink,
    surfaceVariant = NewsHubColors.SurfaceVariantLight,
    onSurfaceVariant = NewsHubColors.TextDimLight,
    outline = NewsHubColors.OutlineLight,
    outlineVariant = NewsHubColors.OutlineLight,
    error = NewsHubColors.Signal,
    onError = Color.White,
    errorContainer = NewsHubColors.Signal.copy(alpha = 0.12f),
    onErrorContainer = NewsHubColors.Signal,
)

private val DarkColors = darkColorScheme(
    primary = NewsHubColors.Signal,
    onPrimary = NewsHubColors.OnSignal,
    primaryContainer = NewsHubColors.Signal.copy(alpha = 0.22f),
    onPrimaryContainer = Color(0xFFFF8A73),
    secondary = NewsHubColors.Wave,
    onSecondary = NewsHubColors.OnWave,
    secondaryContainer = NewsHubColors.Wave.copy(alpha = 0.2f),
    onSecondaryContainer = NewsHubColors.Wave,
    tertiary = NewsHubColors.Gold,
    onTertiary = NewsHubColors.OnGold,
    background = NewsHubColors.PageBgDark,
    onBackground = NewsHubColors.TextDark,
    surface = NewsHubColors.SurfaceDark,
    onSurface = NewsHubColors.TextDark,
    surfaceVariant = NewsHubColors.SurfaceVariantDark,
    onSurfaceVariant = NewsHubColors.TextDimDark,
    outline = NewsHubColors.OutlineDark,
    outlineVariant = NewsHubColors.OutlineDark,
    error = NewsHubColors.Signal,
    onError = Color.White,
    errorContainer = NewsHubColors.Signal.copy(alpha = 0.22f),
    onErrorContainer = Color(0xFFFF8A73),
)

@Composable
fun NewsHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = NewsHubTypography,
        shapes = NewsHubShapes,
        content = content,
    )
}
