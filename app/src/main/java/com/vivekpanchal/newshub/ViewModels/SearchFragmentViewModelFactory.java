package com.vivekpanchal.newshub.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import com.vivekpanchal.newshub.models.NewsFeedModel;

public class SearchFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ArrayList<NewsFeedModel> mNewsFeeds;

    public SearchFragmentViewModelFactory(ArrayList<NewsFeedModel> mFeeds){
        mNewsFeeds = mFeeds;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SearchFragmentViewModel(mNewsFeeds);
    }
}
