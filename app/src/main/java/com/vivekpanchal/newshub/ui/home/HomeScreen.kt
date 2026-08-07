package com.vivekpanchal.newshub.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.ui.common.CategoryChipRow
import com.vivekpanchal.newshub.ui.common.EmptyContent
import com.vivekpanchal.newshub.ui.common.ErrorContent
import com.vivekpanchal.newshub.ui.common.NewsListItem
import com.vivekpanchal.newshub.ui.common.ShimmerFeedList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.NavigateToDetail -> onArticleClick(effect.article)
            }
        }
    }

    when {
        state.isLoading -> ShimmerFeedList()
        state.isError -> ErrorContent(onRetry = { viewModel.setIntent(HomeIntent.Retry) })
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 8.dp),
        ) {
            if (state.breakingArticles.isNotEmpty()) {
                item(key = "breaking_header") {
                    HomeSectionHeader(label = "Breaking now", live = true)
                }
                item(key = "breaking_carousel") {
                    BreakingCarousel(
                        articles = state.breakingArticles,
                        onClick = { viewModel.setIntent(HomeIntent.ArticleClicked(it)) },
                    )
                }
            }

            if (state.trendingArticles.isNotEmpty()) {
                item(key = "trending_header") {
                    HomeSectionHeader(label = "Trending now")
                }
                itemsIndexed(state.trendingArticles, key = { _, article -> article.headline }) { index, article ->
                    TrendingRailItem(
                        rank = index + 1,
                        article = article,
                        onClick = { viewModel.setIntent(HomeIntent.ArticleClicked(article)) },
                    )
                }
            }

            item(key = "category_chips") {
                CategoryChipRow(
                    categories = HomeCategories.ALL,
                    selected = state.selectedCategory,
                    onSelect = { viewModel.setIntent(HomeIntent.CategorySelected(it)) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                )
            }

            if (state.isEmpty) {
                item(key = "empty") {
                    EmptyContent(message = stringResource(R.string.no_stories_found))
                }
            } else {
                items(state.feedArticles, key = { it.headline }) { article ->
                    NewsListItem(
                        article = article,
                        onClick = { viewModel.setIntent(HomeIntent.ArticleClicked(article)) },
                    )
                }
            }
        }
    }
}
