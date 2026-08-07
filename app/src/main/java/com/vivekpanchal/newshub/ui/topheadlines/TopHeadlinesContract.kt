package com.vivekpanchal.newshub.ui.topheadlines

import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.mvi.UiEffect
import com.vivekpanchal.newshub.util.mvi.UiIntent
import com.vivekpanchal.newshub.util.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TopHeadlinesState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val articles: ImmutableList<Article> = persistentListOf(),
) : UiState

sealed interface TopHeadlinesIntent : UiIntent {
    data object Load : TopHeadlinesIntent
    data object Retry : TopHeadlinesIntent
    data class ArticleClicked(val article: Article) : TopHeadlinesIntent
}

sealed interface TopHeadlinesEffect : UiEffect {
    data class NavigateToDetail(val article: Article) : TopHeadlinesEffect
}
