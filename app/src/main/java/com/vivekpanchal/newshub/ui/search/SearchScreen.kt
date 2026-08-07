package com.vivekpanchal.newshub.ui.search

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.ui.common.EmptyContent
import com.vivekpanchal.newshub.ui.common.ErrorContent
import com.vivekpanchal.newshub.ui.common.NewsListItem
import com.vivekpanchal.newshub.ui.common.ShimmerFeedList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SearchEffect.NavigateToDetail -> onArticleClick(effect.article)
                SearchEffect.ShowEmptyQueryMessage ->
                    Toast.makeText(context, R.string.toast_message_enter_search_text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.setIntent(SearchIntent.QueryChanged(it)) },
                label = { Text(stringResource(R.string.search_query_hint)) },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = { viewModel.setIntent(SearchIntent.Search) },
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(stringResource(R.string.search_btn_label))
            }
        }

        when {
            state.isLoading -> ShimmerFeedList()
            state.isError -> ErrorContent(onRetry = { viewModel.setIntent(SearchIntent.Retry) })
            !state.hasSearched -> EmptyContent(message = stringResource(R.string.search_prompt_message))
            state.articles.isEmpty() -> EmptyContent(message = stringResource(R.string.no_search_results))
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(state.articles) { article ->
                    NewsListItem(
                        article = article,
                        onClick = { viewModel.setIntent(SearchIntent.ArticleClicked(article)) },
                    )
                }
            }
        }
    }
}
