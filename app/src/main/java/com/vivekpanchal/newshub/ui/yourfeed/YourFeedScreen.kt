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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.ui.common.CategoryChipRow
import com.vivekpanchal.newshub.ui.common.ErrorContent
import com.vivekpanchal.newshub.ui.common.NewsListItem
import com.vivekpanchal.newshub.ui.common.ShimmerFeedList
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

    Column(modifier = Modifier.fillMaxSize()) {
        if (state.userInterests.isNotEmpty()) {
            CategoryChipRow(
                categories = state.userInterests,
                selected = state.selectedCategory,
                onSelect = { viewModel.setIntent(YourFeedIntent.CategorySelected(it)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            )
        }

        when {
            state.isLoading -> ShimmerFeedList()
            state.isError -> ErrorContent(onRetry = { viewModel.setIntent(YourFeedIntent.Retry) })
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(state.articles) { article ->
                    NewsListItem(
                        article = article,
                        onClick = { viewModel.setIntent(YourFeedIntent.ArticleClicked(article)) },
                    )
                }
            }
        }
    }
}
