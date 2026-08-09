package com.vivekpanchal.newshub.ui.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.domain.model.stableKey
import com.vivekpanchal.newshub.ui.common.EmptyContent
import com.vivekpanchal.newshub.ui.common.NewsListItem
import com.vivekpanchal.newshub.ui.common.PreviewSampleData
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FavoritesScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is FavoritesEffect.NavigateToDetail -> onArticleClick(effect.article)
            }
        }
    }

    FavoritesContent(
        state = state,
        onArticleClick = { viewModel.setIntent(FavoritesIntent.ArticleClicked(it)) },
    )
}

@Composable
private fun FavoritesContent(state: FavoritesState, onArticleClick: (Article) -> Unit) {
    if (state.articles.isEmpty()) {
        EmptyContent(message = stringResource(R.string.no_favorites_found))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(state.articles, key = { it.stableKey }) { article ->
                NewsListItem(article = article, onClick = { onArticleClick(article) })
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun FavoritesContentPreview() {
    NewsHubPreviewSurface {
        FavoritesContent(state = FavoritesState(articles = PreviewSampleData.articlesImmutable), onArticleClick = {})
    }
}

@PreviewLightDark
@Composable
private fun FavoritesContentEmptyPreview() {
    NewsHubPreviewSurface {
        FavoritesContent(state = FavoritesState(articles = persistentListOf()), onArticleClick = {})
    }
}
