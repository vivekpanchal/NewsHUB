package com.vivekpanchal.newshub.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsHeadlineDao {

    @Query("SELECT * FROM news_headlines ORDER BY id DESC")
    fun loadAllNewsHeadlines(): Flow<List<NewsHeadlineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsHeadline(headlineEntity: NewsHeadlineEntity)

    @Delete
    suspend fun deleteNewsHeadline(headlineEntity: NewsHeadlineEntity)

    /** Matches by URL; falls back to headline only for articles that have no URL. */
    @Query("SELECT * FROM news_headlines WHERE news_url = :url OR (:url IS NULL AND news_headline = :headline) LIMIT 1")
    fun loadFavorite(url: String?, headline: String): Flow<NewsHeadlineEntity?>
}
