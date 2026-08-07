package com.vivekpanchal.newshub.ui.yourfeed

import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.mvi.UiEffect
import com.vivekpanchal.newshub.mvi.UiIntent
import com.vivekpanchal.newshub.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class YourFeedState(
    val userInterests: ImmutableList<String> = persistentListOf(),
    val selectedCategory: String? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val articles: ImmutableList<Article> = persistentListOf(),
) : UiState

sealed interface YourFeedIntent : UiIntent {
    data object Load : YourFeedIntent
    data object Retry : YourFeedIntent
    data class CategorySelected(val category: String) : YourFeedIntent
    data class ArticleClicked(val article: Article) : YourFeedIntent
}

sealed interface YourFeedEffect : UiEffect {
    data class NavigateToDetail(val article: Article) : YourFeedEffect
}
