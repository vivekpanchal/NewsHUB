package com.vivekpanchal.newshub.data.repository

import com.vivekpanchal.newshub.data.remote.NewsApi
import com.vivekpanchal.newshub.data.remote.dto.NewsHeadlineResponseDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class NewsRepositoryImplTest {

    private fun repo(result: () -> Response<NewsHeadlineResponseDto>) = NewsRepositoryImpl(
        object : NewsApi {
            override suspend fun getTopHeadlines(country: String, language: String, pageSize: Int) = result()
            override suspend fun searchNews(query: String, language: String, pageSize: Int) = result()
        },
    )

    private fun errorResponse(code: Int) = Response.error<NewsHeadlineResponseDto>(code, "".toResponseBody())

    @Test
    fun `maps HTTP failures to their cause`() = runBlocking {
        assertEquals(NewsResult.Error(NewsError.Unauthorized), repo { errorResponse(401) }.getTopHeadlines())
        assertEquals(NewsResult.Error(NewsError.RateLimited), repo { errorResponse(429) }.getTopHeadlines())
        assertEquals(NewsResult.Error(NewsError.Network), repo { errorResponse(504) }.searchNews("x"))
        assertEquals(NewsResult.Error(NewsError.Unknown), repo { errorResponse(500) }.searchNews("x"))
    }

    @Test
    fun `maps IOException to Network`() = runBlocking {
        assertEquals(NewsResult.Error(NewsError.Network), repo { throw IOException() }.getTopHeadlines())
    }

    @Test(expected = CancellationException::class)
    fun `rethrows cancellation instead of reporting an error`() {
        runBlocking { repo { throw CancellationException() }.getTopHeadlines() }
    }
}
