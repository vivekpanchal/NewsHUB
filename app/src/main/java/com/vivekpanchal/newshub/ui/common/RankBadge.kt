package com.vivekpanchal.newshub.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vivekpanchal.newshub.ui.theme.NewsHubExtraType
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface

/** Gold rank number for the Trending rail - "01", "02", ... - used sparingly, per the design system. */
@Composable
fun RankBadge(rank: Int, modifier: Modifier = Modifier) {
    Text(
        text = rank.coerceIn(1, 99).toString().padStart(2, '0'),
        style = NewsHubExtraType.data.copy(fontSize = 16.sp),
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun RankBadgePreview() {
    NewsHubPreviewSurface {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
            RankBadge(rank = 1)
            RankBadge(rank = 2)
            RankBadge(rank = 3)
        }
    }
}
