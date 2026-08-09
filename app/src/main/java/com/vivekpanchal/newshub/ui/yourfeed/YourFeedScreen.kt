package com.vivekpanchal.newshub.ui.yourfeed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.ui.common.CategoryChipRow
import com.vivekpanchal.newshub.ui.common.ErrorContent
import com.vivekpanchal.newshub.ui.common.NewsListItem
import com.vivekpanchal.newshub.ui.common.PreviewSampleData
import com.vivekpanchal.newshub.ui.common.ShimmerFeedList
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest

@Composable
fun YourFeedScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: YourFeedViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is YourFeedEffect.NavigateToDetail -> onArticleClick(effect.article)
            }
        }
    }

    YourFeedContent(
        state = state,
        onArticleClick = { viewModel.setIntent(YourFeedIntent.ArticleClicked(it)) },
        onCategorySelected = { viewModel.setIntent(YourFeedIntent.CategorySelected(it)) },
        onRetry = { viewModel.setIntent(YourFeedIntent.Retry) },
    )
}

@Composable
private fun YourFeedContent(
    state: YourFeedState,
    onArticleClick: (Article) -> Unit,
    onCategorySelected: (String) -> Unit,
    onRetry: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (state.userInterests.isNotEmpty()) {
            CategoryChipRow(
                categories = state.userInterests,
                selected = state.selectedCategory,
                onSelect = onCategorySelected,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            )
        }

        when {
            state.isLoading -> ShimmerFeedList()
            state.isError -> ErrorContent(onRetry = onRetry)
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(state.articles) { article ->
                    NewsListItem(article = article, onClick = { onArticleClick(article) })
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun YourFeedContentPreview() {
    NewsHubPreviewSurface {
        YourFeedContent(
            state = YourFeedState(
                userInterests = persistentListOf("Technology", "Startups", "Travel"),
                selectedCategory = "Technology",
                isLoading = false,
                articles = PreviewSampleData.articlesImmutable,
            ),
            onArticleClick = {},
            onCategorySelected = {},
            onRetry = {},
        )
    }
}
