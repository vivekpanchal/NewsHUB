package com.vivekpanchal.newshub.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Wraps @Preview content in the real theme + a themed background, mirroring MainActivity's setup. */
@Composable
fun NewsHubPreviewSurface(content: @Composable () -> Unit) {
    NewsHubTheme {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}
