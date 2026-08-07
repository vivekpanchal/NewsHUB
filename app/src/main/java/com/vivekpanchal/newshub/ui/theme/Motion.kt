package com.vivekpanchal.newshub.ui.theme

import android.provider.Settings
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

object MotionTokens {
    const val FAST_MS = 150
    const val MEDIUM_MS = 280
    const val SLOW_MS = 420
}

/** Feed/card spring: slight overshoot, no bounce-past. Reused across pagers, chip indicators, likes. */
fun <T> newsHubSpring(): SpringSpec<T> = spring(dampingRatio = 0.86f, stiffness = Spring.StiffnessMediumLow)

/**
 * Mirrors the OS "remove animations" accessibility setting (Settings.Global.ANIMATOR_DURATION_SCALE == 0),
 * which is what Android exposes for a user's reduced-motion preference. Composables that animate purely
 * for decoration (pulses, marquees) should skip the animation entirely when this is true.
 */
@Composable
fun isReducedMotionEnabled(): Boolean {
    val context = LocalContext.current
    return remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
}
