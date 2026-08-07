package com.vivekpanchal.newshub.ui.topheadlines

import com.vivekpanchal.newshub.data.repository.NewsRepository
import com.vivekpanchal.newshub.data.repository.NewsResult
import com.vivekpanchal.newshub.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : MviViewModel<TopHeadlinesState, TopHeadlinesIntent, TopHeadlinesEffect>(TopHeadlinesState()) {

    init {
        setIntent(TopHeadlinesIntent.Load)
    }

    override suspend fun handleIntent(intent: TopHeadlinesIntent) {
        when (intent) {
            is TopHeadlinesIntent.Load, is TopHeadlinesIntent.Retry -> loadTopHeadlines()
            is TopHeadlinesIntent.ArticleClicked ->
                setEffect { TopHeadlinesEffect.NavigateToDetail(intent.article) }
        }
    }

    private suspend fun loadTopHeadlines() {
        setState { copy(isLoading = true, isError = false) }
        when (val result = newsRepository.getTopHeadlines()) {
            is NewsResult.Success -> setState { copy(isLoading = false, articles = result.articles) }
            is NewsResult.Error -> setState { copy(isLoading = false, isError = true) }
        }
    }
}
