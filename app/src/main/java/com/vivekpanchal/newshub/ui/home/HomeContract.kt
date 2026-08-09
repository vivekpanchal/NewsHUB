package com.vivekpanchal.newshub.ui.home

import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.mvi.UiEffect
import com.vivekpanchal.newshub.util.mvi.UiIntent
import com.vivekpanchal.newshub.util.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Home's category chip row. [BREAKING] is the default view (getTopHeadlines); the rest query by name. */
object HomeCategories {
    const val BREAKING = "Breaking"
    val ALL = listOf(
        BREAKING, "India", "World", "Technology", "Business", "Sports",
        "Entertainment", "Science", "Gaming", "AI", "Startups",
    )
}

data class HomeState(
    val selectedCategory: String = HomeCategories.BREAKING,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
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
