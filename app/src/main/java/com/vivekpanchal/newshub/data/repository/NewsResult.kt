package com.vivekpanchal.newshub.data.repository

import com.vivekpanchal.newshub.domain.model.Article

sealed interface NewsResult {
    data class Success(val articles: List<Article>) : NewsResult
    data class Error(val reason: NewsError) : NewsResult
}

enum class NewsError { Network, Unauthorized, RateLimited, Unknown }
