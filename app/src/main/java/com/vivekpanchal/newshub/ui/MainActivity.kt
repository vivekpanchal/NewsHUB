package com.vivekpanchal.newshub.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.vivekpanchal.newshub.ui.navigation.NewsHubNavHost
import com.vivekpanchal.newshub.ui.theme.NewsHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsHubTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NewsHubNavHost()
                }
            }
        }
    }
}
