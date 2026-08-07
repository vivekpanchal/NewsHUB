package com.vivekpanchal.newshub.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface
import com.vivekpanchal.newshub.ui.theme.PillShape
import com.vivekpanchal.newshub.ui.theme.newsHubSpring

/** A single pill chip with an animated fill/border transition between selected and unselected. */
@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        animationSpec = newsHubSpring(),
        label = "chipContainerColor",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = newsHubSpring(),
        label = "chipContentColor",
    )

    Surface(
        onClick = onClick,
        shape = PillShape,
        color = containerColor,
        contentColor = contentColor,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
        )
    }
}

/** Horizontally scrollable row of [CategoryChip]s. */
@Composable
fun CategoryChipRow(
    categories: List<String>,
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        items(categories) { category ->
            CategoryChip(
                label = category,
                selected = category == selected,
                onClick = { onSelect(category) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun CategoryChipRowPreview() {
    NewsHubPreviewSurface {
        CategoryChipRow(
            categories = listOf("Breaking", "India", "World", "Technology", "Business"),
            selected = "India",
            onSelect = {},
            modifier = Modifier.padding(vertical = 12.dp),
        )
    }
}
