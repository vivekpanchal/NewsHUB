package com.vivekpanchal.newshub.ui.search

import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.mvi.UiEffect
import com.vivekpanchal.newshub.util.mvi.UiIntent
import com.vivekpanchal.newshub.util.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val hasSearched: Boolean = false,
    val articles: ImmutableList<Article> = persistentListOf(),
) : UiState

sealed interface SearchIntent : UiIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data object Search : SearchIntent
    data object Retry : SearchIntent
    data class ArticleClicked(val article: Article) : SearchIntent
}

sealed interface SearchEffect : UiEffect {
    data object ShowEmptyQueryMessage : SearchEffect
    data class NavigateToDetail(val article: Article) : SearchEffect
}
