package com.vivekpanchal.newshub.data.remote

import com.vivekpanchal.newshub.data.remote.dto.NewsHeadlineResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
        @Query("language") language: String,
    ): Response<NewsHeadlineResponseDto>

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("language") language: String,
    ): Response<NewsHeadlineResponseDto>

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
    }
}
