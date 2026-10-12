package com.vivekpanchal.newshub.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vivekpanchal.newshub.ui.theme.NewsHubExtraType
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface
import com.vivekpanchal.newshub.ui.theme.Spacing
import com.vivekpanchal.newshub.ui.theme.isReducedMotionEnabled

/** A single pulsing dot - the "something is live" tell, reused standalone or inside [LiveBadge]. */
@Composable
fun PulseDot(color: Color, modifier: Modifier = Modifier, size: Dp = 6.dp) {
    val reducedMotion = isReducedMotionEnabled()
    val scale = if (reducedMotion) {
        1f
    } else {
        val transition = rememberInfiniteTransition(label = "pulse")
        val animatedScale by transition.animateFloat(
            initialValue = 1f,
            targetValue = 1.9f,
            animationSpec = infiniteRepeatable(tween(1600), repeatMode = RepeatMode.Reverse),
            label = "pulseScale",
        )
        animatedScale
    }
    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .clip(RoundedCornerShape(50))
            .background(color),
    )
}

/** Pill badge for "BREAKING" / "LIVE" tags - mono eyebrow type, pulsing dot, Signal-colored by default. */
@Composable
fun LiveBadge(
    label: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = Spacing.sm, vertical = 4.dp),
    ) {
        PulseDot(color = contentColor, size = 5.dp)
        Text(text = label, style = NewsHubExtraType.eyebrow, color = contentColor)
    }
}

@PreviewLightDark
@Composable
private fun LiveBadgePreview() {
    NewsHubPreviewSurface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.padding(Spacing.lg),
        ) {
            LiveBadge(label = "BREAKING")
            LiveBadge(label = "LIVE")
        }
    }
}
