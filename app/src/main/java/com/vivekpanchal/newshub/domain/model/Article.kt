package com.vivekpanchal.newshub.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** UI/domain representation of a single news article, independent of network or DB shape. */
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
