package com.vivekpanchal.newshub.ui.topheadlines

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.ui.common.ErrorContent
import com.vivekpanchal.newshub.ui.common.LoadingContent
import com.vivekpanchal.newshub.ui.common.NewsListItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TopHeadlinesScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is TopHeadlinesEffect.NavigateToDetail -> onArticleClick(effect.article)
            }
        }
    }

    when {
        state.isLoading -> LoadingContent()
        state.isError -> ErrorContent(onRetry = { viewModel.setIntent(TopHeadlinesIntent.Retry) })
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(state.articles) { article ->
                NewsListItem(
                    article = article,
                    onClick = { viewModel.setIntent(TopHeadlinesIntent.ArticleClicked(article)) },
                )
            }
        }
    }
}
