package com.vivekpanchal.newshub.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NewsHeadlineDao {

    @Query("SELECT * FROM news_headlines ORDER BY id")
    LiveData<List<NewsHeadlineEntity>> loadAllNewsHeadlines();

    @Insert
    void insertNewsHeadline(NewsHeadlineEntity headlineEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNewsHeadline(NewsHeadlineEntity headlineEntity);

    @Delete
    void deleteNewsHeadline(NewsHeadlineEntity headlineEntity);

    @Query("SELECT * FROM news_headlines WHERE news_headline = :headline")
    LiveData<NewsHeadlineEntity> loadNewsByHeadline(String headline);
}
