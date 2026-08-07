package com.vivekpanchal.newshub.ui.detail

import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.mvi.UiEffect
import com.vivekpanchal.newshub.util.mvi.UiIntent
import com.vivekpanchal.newshub.util.mvi.UiState

data class NewsDetailState(
    val article: Article,
    val isFavorite: Boolean = false,
) : UiState

sealed interface NewsDetailIntent : UiIntent {
    data object ToggleFavorite : NewsDetailIntent
    data object Share : NewsDetailIntent
    data object OpenInBrowser : NewsDetailIntent
}

sealed interface NewsDetailEffect : UiEffect {
    data class ShowMessage(val messageResId: Int) : NewsDetailEffect
    data class ShareUrl(val url: String) : NewsDetailEffect
    data class OpenUrlInBrowser(val url: String) : NewsDetailEffect
}
