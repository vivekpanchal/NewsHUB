package com.vivekpanchal.newshub.domain.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/** UI/domain representation of a single news article, independent of network or DB shape. */
// @JsonClass is here solely so ArticleNavType can (de)serialize this as a nav-route string arg.
@JsonClass(generateAdapter = true)
@Parcelize
data class Article(
    val headline: String,
    val publishedAt: String,
    val imageUrl: String?,
    val authorName: String?,
    val description: String?,
    val newsSource: String?,
    val newsUrl: String?,
) : Parcelable

/** Stable identity for dedup/Compose-list-keys: [newsUrl] when present, else headline+timestamp. */
val Article.stableKey: String
    get() = newsUrl?.takeIf { it.isNotBlank() } ?: "$headline|$publishedAt"
