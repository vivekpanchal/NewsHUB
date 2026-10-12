package com.vivekpanchal.newshub.data.repository

import com.vivekpanchal.newshub.data.remote.NewsApi
import com.vivekpanchal.newshub.data.remote.dto.ArticleDto
import com.vivekpanchal.newshub.data.remote.dto.NewsHeadlineResponseDto
import com.vivekpanchal.newshub.domain.model.Article
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
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

    override suspend fun getTopHeadlines(country: String): NewsResult = fetch {
        api.getTopHeadlines(country = country, language = LANGUAGE_ENGLISH, pageSize = PAGE_SIZE)
    }

    override suspend fun searchNews(query: String): NewsResult = fetch {
        api.searchNews(query = query, language = LANGUAGE_ENGLISH, pageSize = PAGE_SIZE)
    }

    private suspend fun fetch(call: suspend () -> Response<NewsHeadlineResponseDto>): NewsResult {
        val response = try {
            call()
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            return NewsResult.Error(NewsError.Network)
        } catch (e: Exception) {
            return NewsResult.Error(NewsError.Unknown)
        }
        if (!response.isSuccessful) {
            return NewsResult.Error(
                when (response.code()) {
                    401 -> NewsError.Unauthorized
                    429 -> NewsError.RateLimited
                    // 504 is what the offline cache fallback returns when nothing is cached.
                    504 -> NewsError.Network
                    else -> NewsError.Unknown
                },
            )
        }
        val articles = response.body()?.articles ?: return NewsResult.Error(NewsError.Unknown)
        return NewsResult.Success(articles.map { it.toDomain() })
    }

    private companion object {
        const val LANGUAGE_ENGLISH = "en"
        // NewsAPI's max page size; the free plan also caps results at 100, so one page is everything.
        const val PAGE_SIZE = 100
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
