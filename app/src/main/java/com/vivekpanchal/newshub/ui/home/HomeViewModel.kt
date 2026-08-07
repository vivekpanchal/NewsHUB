package com.vivekpanchal.newshub.ui.home

import com.vivekpanchal.newshub.data.repository.NewsRepository
import com.vivekpanchal.newshub.data.repository.NewsResult
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.util.isRecentEnoughToBeBreaking
import com.vivekpanchal.newshub.util.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    init {
        setIntent(HomeIntent.Load)
    }

    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Load, is HomeIntent.Retry -> loadFeed(currentState.selectedCategory)
            is HomeIntent.CategorySelected -> {
                if (intent.category != currentState.selectedCategory) {
                    setState { copy(selectedCategory = intent.category) }
                    loadFeed(intent.category)
                }
            }
            is HomeIntent.ArticleClicked -> setEffect { HomeEffect.NavigateToDetail(intent.article) }
        }
    }

    private suspend fun loadFeed(category: String) {
        setState { copy(isLoading = true, isError = false) }

        val result = if (category == HomeCategories.BREAKING) {
            newsRepository.getTopHeadlines()
        } else {
            newsRepository.searchNews(category)
        }

        when (result) {
            is NewsResult.Success -> {
                val (breaking, trending, feed) = deriveSections(result.articles)
                setState {
                    copy(
                        isLoading = false,
                        isError = false,
                        breakingArticles = breaking.toImmutableList(),
                        trendingArticles = trending.toImmutableList(),
                        feedArticles = feed.toImmutableList(),
                    )
                }
            }
            is NewsResult.Error -> setState { copy(isLoading = false, isError = true) }
        }
    }

    /**
     * No API flags "breaking" or "trending" - breaking is a recency heuristic over the front of the
     * response, trending is the response's own editorial order. The feed below excludes whatever
     * trending already surfaced, so nothing repeats on one screen.
     */
    private fun deriveSections(articles: List<Article>): Triple<List<Article>, List<Article>, List<Article>> {
        val breaking = articles.take(8).filter { isRecentEnoughToBeBreaking(it.publishedAt) }.take(3)
        val trending = articles.take(6)
        val feed = articles.drop(6)
        return Triple(breaking, trending, feed)
    }
}
