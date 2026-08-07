package com.vivekpanchal.newshub.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.data.repository.FavoritesRepository
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.mvi.MviViewModel
import com.vivekpanchal.newshub.ui.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val favoritesRepository: FavoritesRepository,
) : MviViewModel<NewsDetailState, NewsDetailIntent, NewsDetailEffect>(
    NewsDetailState(article = requireNotNull(savedStateHandle.get<Article>(NavRoutes.DETAIL_ARG))),
) {

    init {
        favoritesRepository.isFavorite(currentState.article.headline)
            .onEach { isFavorite -> setState { copy(isFavorite = isFavorite) } }
            .launchIn(viewModelScope)
    }

    override suspend fun handleIntent(intent: NewsDetailIntent) {
        when (intent) {
            is NewsDetailIntent.ToggleFavorite -> toggleFavorite()
            is NewsDetailIntent.Share -> share()
            is NewsDetailIntent.OpenInBrowser -> openInBrowser()
        }
    }

    private suspend fun toggleFavorite() {
        val article = currentState.article
        if (currentState.isFavorite) {
            favoritesRepository.removeFavorite(article)
            setEffect { NewsDetailEffect.ShowMessage(R.string.removed_from_fav) }
        } else {
            favoritesRepository.addFavorite(article)
            setEffect { NewsDetailEffect.ShowMessage(R.string.added_to_fav) }
        }
    }

    private fun share() {
        currentState.article.newsUrl?.let { url -> setEffect { NewsDetailEffect.ShareUrl(url) } }
    }

    private fun openInBrowser() {
        currentState.article.newsUrl?.let { url -> setEffect { NewsDetailEffect.OpenUrlInBrowser(url) } }
    }
}
