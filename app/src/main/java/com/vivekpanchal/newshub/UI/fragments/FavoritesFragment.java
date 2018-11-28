package com.vivekpanchal.newshub.UI.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vivekpanchal.newshub.R;
import com.vivekpanchal.newshub.ViewModels.FavoritesFragmentViewModel;

import java.util.ArrayList;
import java.util.List;


import com.vivekpanchal.newshub.UI.activities.NewsDetailDisplayActivity;
import com.vivekpanchal.newshub.adapters.NewsHeadlineRecyclerViewAdapter;
import com.vivekpanchal.newshub.database.NewsHeadlineEntity;
import com.vivekpanchal.newshub.models.NewsFeedModel;
import com.vivekpanchal.newshub.utility.Constants;

public class FavoritesFragment extends Fragment {

    private RecyclerView favouritesRecyclerView;
    private ArrayList<NewsFeedModel> mNewsFeeds;
    private NewsHeadlineRecyclerViewAdapter adapter;
    private LinearLayout noFavoritesLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        setUpViews(view);

        // Retrieving data from the database
        loadAllFavoriteNewsFeed();
        return view;
    }

    private void loadAllFavoriteNewsFeed() {

        FavoritesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(FavoritesFragmentViewModel.class);
        viewModel.getmFeeds().observe(getActivity() != null ? getActivity(): getViewLifecycleOwner(), new Observer<List<NewsHeadlineEntity>>() {
            @Override
            public void onChanged(@Nullable List<NewsHeadlineEntity> newsHeadlineEntities) {
                if (newsHeadlineEntities != null) {
                    mNewsFeeds.clear();
                    for (NewsHeadlineEntity headlineEntity : newsHeadlineEntities) {
                        mNewsFeeds.add(new NewsFeedModel(headlineEntity.getHeadline(),
                                headlineEntity.getDate(), headlineEntity.getImageUrl(),
                                headlineEntity.getAuthorName(), headlineEntity.getDescription(),
                                headlineEntity.getNewsSource(), headlineEntity.getNewsUrl()));
                    }
                    adapter.notifyDataSetChanged();
                    if (mNewsFeeds.size() > 0) {
                        noFavoritesLayout.setVisibility(View.INVISIBLE);
                    } else {
                        noFavoritesLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void setUpViews(View view) {
        favouritesRecyclerView = view.findViewById(R.id.rv_frag_favorites);
        noFavoritesLayout = view.findViewById(R.id.ll_no_fav_frag_favorites);

        noFavoritesLayout.setVisibility(View.VISIBLE);

        mNewsFeeds = new ArrayList<>();
        adapter = new NewsHeadlineRecyclerViewAdapter(getActivity(), mNewsFeeds);

        adapter.setOnItemClickListener(new NewsHeadlineRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent i = new Intent(getContext(), NewsDetailDisplayActivity.class);
                i.putExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY, mNewsFeeds.get(position));
                startActivity(i);
            }
        });

        favouritesRecyclerView.setAdapter(adapter);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));
    }
}
