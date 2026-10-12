package com.vivekpanchal.newshub.ui.home

import com.vivekpanchal.newshub.data.repository.NewsError
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.mvi.UiEffect
import com.vivekpanchal.newshub.util.mvi.UiIntent
import com.vivekpanchal.newshub.util.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Home's category chip row; each queries /everything by name. No "Breaking"/top-headlines entry:
 * NewsAPI's top-headlines returns nothing for country=in, so that tab was always empty.
 */
object HomeCategories {
    val ALL = listOf(
        "India", "World", "Technology", "Business", "Sports",
        "Entertainment", "Science", "Gaming", "AI", "Startups",
    )
}

data class HomeState(
    val selectedCategory: String = HomeCategories.ALL.first(),
    val isLoading: Boolean = true,
    val error: NewsError? = null,
    // No API exposes real engagement/trending metrics - breaking is a recency heuristic, trending
    // is response order. Nothing here is a fabricated number (see NewsResult / DateFormatter).
    val breakingArticles: ImmutableList<Article> = persistentListOf(),
    val trendingArticles: ImmutableList<Article> = persistentListOf(),
    val feedArticles: ImmutableList<Article> = persistentListOf(),
) : UiState {
    val isEmpty: Boolean
        get() = breakingArticles.isEmpty() && trendingArticles.isEmpty() && feedArticles.isEmpty()
}

sealed interface HomeIntent : UiIntent {
    data object Load : HomeIntent
    data object Retry : HomeIntent
    data class CategorySelected(val category: String) : HomeIntent
    data class ArticleClicked(val article: Article) : HomeIntent
}

sealed interface HomeEffect : UiEffect {
    data class NavigateToDetail(val article: Article) : HomeEffect
}
