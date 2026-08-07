package com.vivekpanchal.newshub.ui.navigation

import com.vivekpanchal.newshub.domain.model.Article

object NavRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"
    const val DETAIL_PATTERN = "detail/{article}"
    const val DETAIL_ARG = "article"

    fun detail(article: Article): String = "detail/${ArticleNavType.serializeAsValue(article)}"
}

object MainTabRoutes {
    const val TOP_HEADLINES = "top_headlines"
    const val YOUR_FEED = "your_feed"
    const val FAVORITES = "favorites"
    const val SEARCH = "search"
}
