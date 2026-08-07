package com.vivekpanchal.newshub.data.repository

import com.vivekpanchal.newshub.BuildConfig
import com.vivekpanchal.newshub.data.remote.NewsApi
import com.vivekpanchal.newshub.data.remote.dto.ArticleDto
import com.vivekpanchal.newshub.data.remote.dto.NewsHeadlineResponseDto
import com.vivekpanchal.newshub.domain.model.Article
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface NewsRepository {
    suspend fun getTopHeadlines(country: String = "in"): NewsResult
    suspend fun searchNews(query: String): NewsResult
}

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
) : NewsRepository {

    override suspend fun getTopHeadlines(country: String): NewsResult = runCatching {
        api.getTopHeadlines(country = country, apiKey = BuildConfig.NEWS_API_KEY, language = LANGUAGE_ENGLISH)
    }.toNewsResult()

    override suspend fun searchNews(query: String): NewsResult = runCatching {
        api.searchNews(query = query, apiKey = BuildConfig.NEWS_API_KEY, language = LANGUAGE_ENGLISH)
    }.toNewsResult()

    private fun Result<Response<NewsHeadlineResponseDto>>.toNewsResult(): NewsResult {
        val response = getOrNull() ?: return NewsResult.Error
        if (!response.isSuccessful) return NewsResult.Error
        val articles = response.body()?.articles ?: return NewsResult.Error
        return NewsResult.Success(articles.map { it.toDomain() })
    }

    private companion object {
        const val LANGUAGE_ENGLISH = "en"
    }
}

private fun ArticleDto.toDomain(): Article = Article(
    headline = title.orEmpty(),
    publishedAt = publishedAt.orEmpty(),
    imageUrl = urlToImage,
    authorName = author,
    description = description ?: content,
    newsSource = source?.name,
    newsUrl = url,
)
