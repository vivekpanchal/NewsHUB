package com.vivekpanchal.newshub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsHeadlineResponseDto(
    @SerializedName("status") val status: String?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<ArticleDto>?,
)

data class ArticleDto(
    @SerializedName("source") val source: SourceDto?,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?,
)

data class SourceDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
)
