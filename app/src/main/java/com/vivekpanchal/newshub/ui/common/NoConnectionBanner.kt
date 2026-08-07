package com.vivekpanchal.newshub.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vivekpanchal.newshub.R

@Composable
fun NoConnectionBanner(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Surface(color = MaterialTheme.colorScheme.errorContainer) {
            Text(
                text = stringResource(R.string.internet_conn_not_available),
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
            )
        }
    }
}
