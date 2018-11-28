package com.vivekpanchal.newshub.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.vivekpanchal.newshub.database.AppDatabase;
import com.vivekpanchal.newshub.database.NewsHeadlineEntity;

import java.util.List;

public class FavoritesFragmentViewModel extends AndroidViewModel {

    private LiveData<List<NewsHeadlineEntity>> mFeeds;

    public FavoritesFragmentViewModel(@NonNull Application application) {
        super(application);
        mFeeds = AppDatabase.getInstance(application).getNewsHeadlinesDao().loadAllNewsHeadlines();
    }

    public LiveData<List<NewsHeadlineEntity>> getmFeeds() {
        return mFeeds;
    }
}
