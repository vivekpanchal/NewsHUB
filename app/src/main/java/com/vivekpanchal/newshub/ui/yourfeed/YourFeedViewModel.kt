package com.vivekpanchal.newshub.ui.yourfeed

import com.vivekpanchal.newshub.data.repository.NewsRepository
import com.vivekpanchal.newshub.data.repository.NewsResult
import com.vivekpanchal.newshub.data.repository.UserPreferencesRepository
import com.vivekpanchal.newshub.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class YourFeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : MviViewModel<YourFeedState, YourFeedIntent, YourFeedEffect>(YourFeedState()) {

    init {
        setIntent(YourFeedIntent.Load)
    }

    override suspend fun handleIntent(intent: YourFeedIntent) {
        when (intent) {
            is YourFeedIntent.Load -> loadUserInterestsAndFeed()
            is YourFeedIntent.Retry -> loadFeed(currentState.selectedCategory)
            is YourFeedIntent.CategorySelected -> {
                if (intent.category != currentState.selectedCategory) {
                    setState { copy(selectedCategory = intent.category) }
                    loadFeed(intent.category)
                }
            }
            is YourFeedIntent.ArticleClicked -> setEffect { YourFeedEffect.NavigateToDetail(intent.article) }
        }
    }

    private suspend fun loadUserInterestsAndFeed() {
        val interests = userPreferencesRepository.userInterests.first()
        val category = currentState.selectedCategory ?: interests.firstOrNull()
        setState { copy(userInterests = interests.toImmutableList(), selectedCategory = category) }
        loadFeed(category)
    }

    private suspend fun loadFeed(category: String?) {
        if (category == null) {
            setState { copy(isLoading = false, isError = true) }
            return
        }
        setState { copy(isLoading = true, isError = false) }
        when (val result = newsRepository.searchNews(category)) {
            is NewsResult.Success ->
                setState { copy(isLoading = false, articles = result.articles.toImmutableList()) }
            is NewsResult.Error -> setState { copy(isLoading = false, isError = true) }
        }
    }
}
