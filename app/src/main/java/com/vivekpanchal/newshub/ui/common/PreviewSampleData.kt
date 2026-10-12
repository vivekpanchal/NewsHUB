package com.vivekpanchal.newshub.ui.common

import com.vivekpanchal.newshub.domain.model.Article
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

/** Shared fixture data for @Preview composables across the app - never used at runtime. */
internal object PreviewSampleData {

    val breakingArticle = Article(
        headline = "Cyclone Ruma makes landfall near Odisha coast",
        publishedAt = "2026-08-07T12:15:00Z",
        imageUrl = null,
        authorName = "Ananya Rao",
        description = "Over 40,000 residents moved to shelters as wind speeds reach 120 km/h.",
        newsSource = "Reuters",
        newsUrl = "https://example.com/cyclone-ruma",
    )

    val trendingArticle = Article(
        headline = "iPhone 17 leak shows under-display camera",
        publishedAt = "2026-08-07T09:40:00Z",
        imageUrl = null,
        authorName = "Marcus Chen",
        description = "Supply-chain sources describe a redesigned front sensor stack.",
        newsSource = "The Verge",
        newsUrl = "https://example.com/iphone-17-leak",
    )

    val standardArticle = Article(
        headline = "Bengaluru startups see record Q3 funding as AI bets pay off",
        publishedAt = "2026-08-07T06:05:00Z",
        imageUrl = null,
        authorName = "Priya Nair",
        description = "Early-stage rounds more than doubled year over year, led by infrastructure and agentic tooling startups.",
        newsSource = "Economic Times",
        newsUrl = "https://example.com/bengaluru-funding",
    )

    val longHeadlineArticle = Article(
        headline = "James Webb telescope spots an 'impossible' early galaxy that shouldn't exist under current models",
        publishedAt = "2026-08-06T18:00:00Z",
        imageUrl = null,
        authorName = null,
        description = null,
        newsSource = "NASA",
        newsUrl = "https://example.com/webb-galaxy",
    )

    val articles = listOf(breakingArticle, trendingArticle, standardArticle, longHeadlineArticle)
    val articlesImmutable = articles.toPersistentList()
    val trendingRail = persistentListOf(trendingArticle, standardArticle, longHeadlineArticle)
    val breakingRail = persistentListOf(breakingArticle, trendingArticle)
}
