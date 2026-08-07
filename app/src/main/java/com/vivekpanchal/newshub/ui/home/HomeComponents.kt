package com.vivekpanchal.newshub.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.ui.common.LiveBadge
import com.vivekpanchal.newshub.ui.common.PreviewSampleData
import com.vivekpanchal.newshub.ui.common.PulseDot
import com.vivekpanchal.newshub.ui.common.RankBadge
import com.vivekpanchal.newshub.ui.theme.NewsHubExtraType
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface
import com.vivekpanchal.newshub.ui.theme.NewsHubShapes
import com.vivekpanchal.newshub.ui.theme.Spacing
import com.vivekpanchal.newshub.util.formatRelativeTime

/** Eyebrow row for a Home section: mono uppercase label, optional pulsing dot for "live" sections. */
@Composable
fun HomeSectionHeader(
    label: String,
    modifier: Modifier = Modifier,
    live: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        modifier = modifier.padding(horizontal = Spacing.lg, vertical = Spacing.sm),
    ) {
        if (live) PulseDot(color = MaterialTheme.colorScheme.primary, size = 6.dp)
        Text(text = label.uppercase(), style = NewsHubExtraType.eyebrow, color = color)
    }
}

/** Horizontally scrolling row of compact [BreakingCard]s. */
@Composable
fun BreakingCarousel(articles: List<Article>, onClick: (Article) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        contentPadding = PaddingValues(horizontal = Spacing.lg),
    ) {
        items(articles, key = { it.headline }) { article ->
            BreakingCard(article = article, onClick = { onClick(article) })
        }
    }
}

@Composable
private fun BreakingCard(article: Article, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(100.dp)
            .clip(NewsHubShapes.medium)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = article.imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading),
            error = painterResource(R.drawable.error_news_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(0f to Color.Transparent, 1f to Color.Black.copy(alpha = 0.8f)),
                    ),
                ),
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            LiveBadge(label = "LIVE")
            Text(
                text = article.headline,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
            )
        }
    }
}

/** Ranked row for the Trending rail: gold rank number, thumbnail, headline, source/time meta. */
@Composable
fun TrendingRailItem(rank: Int, article: Article, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
    ) {
        RankBadge(rank = rank, modifier = Modifier.width(28.dp))
        AsyncImage(
            model = article.imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading),
            error = painterResource(R.drawable.error_news_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(10.dp)),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = article.headline,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
            )
            Text(
                text = buildString {
                    if (!article.newsSource.isNullOrBlank()) {
                        append(article.newsSource.uppercase())
                        append(" · ")
                    }
                    append(formatRelativeTime(article.publishedAt).uppercase())
                },
                style = NewsHubExtraType.eyebrow,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeSectionHeaderPreview() {
    NewsHubPreviewSurface {
        Column {
            HomeSectionHeader(label = "Breaking now", live = true)
            HomeSectionHeader(label = "Trending now")
        }
    }
}

@PreviewLightDark
@Composable
private fun BreakingCarouselPreview() {
    NewsHubPreviewSurface {
        BreakingCarousel(articles = PreviewSampleData.breakingRail, onClick = {})
    }
}

@PreviewLightDark
@Composable
private fun TrendingRailItemPreview() {
    NewsHubPreviewSurface {
        Column {
            PreviewSampleData.trendingRail.forEachIndexed { index, article ->
                TrendingRailItem(rank = index + 1, article = article, onClick = {})
            }
        }
    }
}
