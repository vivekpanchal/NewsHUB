package com.vivekpanchal.newshub.ui.favorites

import androidx.lifecycle.viewModelScope
import com.vivekpanchal.newshub.data.repository.FavoritesRepository
import com.vivekpanchal.newshub.util.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoritesRepository: FavoritesRepository,
) : MviViewModel<FavoritesState, FavoritesIntent, FavoritesEffect>(FavoritesState()) {

    init {
        favoritesRepository.getAllFavorites()
            .onEach { articles -> setState { copy(articles = articles.toImmutableList()) } }
            .launchIn(viewModelScope)
    }

    override suspend fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.ArticleClicked ->
                setEffect { FavoritesEffect.NavigateToDetail(intent.article) }
        }
    }
}
