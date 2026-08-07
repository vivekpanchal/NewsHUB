package com.vivekpanchal.newshub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = NewsHubPrimary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = NewsHubPrimaryLight,
    secondary = NewsHubAccent,
    onSecondary = androidx.compose.ui.graphics.Color.White,
)

private val DarkColors = darkColorScheme(
    primary = NewsHubPrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.Black,
    primaryContainer = NewsHubPrimaryDark,
    secondary = NewsHubAccent,
    onSecondary = androidx.compose.ui.graphics.Color.White,
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
        content = content,
    )
}
