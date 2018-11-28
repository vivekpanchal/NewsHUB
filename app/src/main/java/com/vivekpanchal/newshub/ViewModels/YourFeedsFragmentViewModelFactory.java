package com.vivekpanchal.newshub.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import com.vivekpanchal.newshub.models.NewsFeedModel;

public class YourFeedsFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ArrayList<NewsFeedModel> mNewsFeeds;
    private final String selectedChoice;

    public YourFeedsFragmentViewModelFactory(ArrayList<NewsFeedModel> mFeeds, String choice){
        mNewsFeeds = mFeeds;
        selectedChoice = choice;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new YourFeedsFragmentViewModel(mNewsFeeds, selectedChoice);
    }
}
