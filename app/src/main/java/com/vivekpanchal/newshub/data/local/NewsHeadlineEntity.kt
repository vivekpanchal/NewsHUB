package com.vivekpanchal.newshub.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vivekpanchal.newshub.domain.model.Article

// The unique index on news_headline, combined with the DAO's insert(OnConflictStrategy.REPLACE),
// makes addFavorite() idempotent: a double-tap before UI state settles updates the existing row
// instead of inserting a duplicate.
@Entity(tableName = "news_headlines", indices = [Index(value = ["news_headline"], unique = true)])
data class NewsHeadlineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "news_headline")
    val headline: String,
    val date: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "author_name")
    val authorName: String?,
    val description: String?,
    @ColumnInfo(name = "news_source")
    val newsSource: String?,
    @ColumnInfo(name = "news_url")
    val newsUrl: String?,
)

fun NewsHeadlineEntity.toDomain(): Article = Article(
    headline = headline,
    publishedAt = date,
    imageUrl = imageUrl,
    authorName = authorName,
    description = description,
    newsSource = newsSource,
    newsUrl = newsUrl,
)

fun Article.toEntity(): NewsHeadlineEntity = NewsHeadlineEntity(
    headline = headline,
    date = publishedAt,
    imageUrl = imageUrl,
    authorName = authorName,
    description = description,
    newsSource = newsSource,
    newsUrl = newsUrl,
)
