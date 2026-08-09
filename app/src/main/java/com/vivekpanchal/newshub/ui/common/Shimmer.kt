package com.vivekpanchal.newshub.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface
import com.vivekpanchal.newshub.ui.theme.isReducedMotionEnabled

/** Shimmering skeleton fill - stands in for a loading card/thumbnail instead of a spinner. */
@Composable
fun Modifier.shimmer(): Modifier {
    val reducedMotion = isReducedMotionEnabled()
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)

    if (reducedMotion) {
        return this.background(base)
    }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shimmerTranslate",
    )
    val brush = Brush.linearGradient(
        colors = listOf(base, highlight, base),
        start = Offset(translate, 0f),
        end = Offset(translate + 400f, 400f),
    )
    return this.background(brush)
}

/** Skeleton placeholder shaped like [com.vivekpanchal.newshub.ui.common.NewsListItem] - for feed loading states. */
@Composable
fun ShimmerFeedCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(20.dp))
            .shimmer(),
    )
}

/** A handful of stacked [ShimmerFeedCard]s, for the initial feed-loading state. */
@Composable
fun ShimmerFeedList(modifier: Modifier = Modifier, count: Int = 4) {
    Column(modifier = modifier.fillMaxSize().padding(PaddingValues(8.dp))) {
        repeat(count) { ShimmerFeedCard() }
    }
}

/** Skeleton row shaped like a compact rank/thumbnail list item - for the Trending rail loading state. */
@Composable
fun ShimmerRailItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier.width(56.dp).height(56.dp).clip(RoundedCornerShape(12.dp)).shimmer(),
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(modifier = Modifier.width(180.dp).height(14.dp).clip(RoundedCornerShape(4.dp)).shimmer())
            Box(modifier = Modifier.width(100.dp).height(10.dp).clip(RoundedCornerShape(4.dp)).shimmer())
        }
    }
}

@PreviewLightDark
@Composable
private fun ShimmerFeedListPreview() {
    NewsHubPreviewSurface { ShimmerFeedList(count = 2) }
}

@PreviewLightDark
@Composable
private fun ShimmerRailItemPreview() {
    NewsHubPreviewSurface {
        Column(modifier = Modifier.padding(16.dp)) {
            ShimmerRailItem()
            ShimmerRailItem()
        }
    }
}
