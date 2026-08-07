package com.vivekpanchal.newshub.ui.favorites

import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.mvi.UiEffect
import com.vivekpanchal.newshub.util.mvi.UiIntent
import com.vivekpanchal.newshub.util.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoritesState(
    val articles: ImmutableList<Article> = persistentListOf(),
) : UiState

sealed interface FavoritesIntent : UiIntent {
    data class ArticleClicked(val article: Article) : FavoritesIntent
}

sealed interface FavoritesEffect : UiEffect {
    data class NavigateToDetail(val article: Article) : FavoritesEffect
}
