package com.vivekpanchal.newshub.ui.search

import com.vivekpanchal.newshub.data.repository.NewsRepository
import com.vivekpanchal.newshub.data.repository.NewsResult
import com.vivekpanchal.newshub.util.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : MviViewModel<SearchState, SearchIntent, SearchEffect>(SearchState()) {

    override suspend fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> setState { copy(query = intent.query) }
            is SearchIntent.Search -> search()
            is SearchIntent.Retry -> search()
            is SearchIntent.ArticleClicked -> setEffect { SearchEffect.NavigateToDetail(intent.article) }
        }
    }

    private suspend fun search() {
        val query = currentState.query.trim()
        if (query.isBlank()) {
            setEffect { SearchEffect.ShowEmptyQueryMessage }
            return
        }
        setState { copy(isLoading = true, isError = false, hasSearched = true) }
        when (val result = newsRepository.searchNews(query)) {
            is NewsResult.Success ->
                setState { copy(isLoading = false, articles = result.articles.toImmutableList()) }
            is NewsResult.Error -> setState { copy(isLoading = false, isError = true) }
        }
    }
}
