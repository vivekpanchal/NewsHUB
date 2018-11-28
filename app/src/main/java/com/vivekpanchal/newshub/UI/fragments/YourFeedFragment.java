package com.vivekpanchal.newshub.UI.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vivekpanchal.newshub.R;
import com.vivekpanchal.newshub.ViewModels.YourFeedsFragmentViewModel;

import java.util.ArrayList;


import com.vivekpanchal.newshub.UI.activities.NewsDetailDisplayActivity;
import com.vivekpanchal.newshub.adapters.NewsHeadlineRecyclerViewAdapter;
import com.vivekpanchal.newshub.ViewModels.YourFeedsFragmentViewModelFactory;
import com.vivekpanchal.newshub.models.Articles;
import com.vivekpanchal.newshub.models.NewsFeedModel;
import com.vivekpanchal.newshub.models.NewsHeadlineResponse;
import com.vivekpanchal.newshub.networking.APIClient;
import com.vivekpanchal.newshub.networking.APIInterface;
import com.vivekpanchal.newshub.utility.Constants;
import com.vivekpanchal.newshub.utility.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourFeedFragment extends Fragment implements View.OnClickListener {

    private Button choiceOne, choiceTwo, choiceThree;
    private RecyclerView yourFeedsRecyclerView;
    private NewsHeadlineRecyclerViewAdapter adapter;
    private ArrayList<NewsFeedModel> mNewsFeeds;
    private String selectedChoice;
    private APIInterface apiInterface;
    private LinearLayout loadingLayout;
    private TextView loadingLayoutMessage;
    private ProgressBar loadingBar;
    private ImageView loadingLayoutImage;
    private Button retryLoadingButton;
    private YourFeedsFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_feed, container, false);

        setUpViews(view);
        YourFeedsFragmentViewModelFactory factory = new YourFeedsFragmentViewModelFactory(mNewsFeeds, selectedChoice);
        viewModel = ViewModelProviders.of(getActivity(), factory).get(YourFeedsFragmentViewModel.class);
        if (savedInstanceState == null) {
            if (!TextUtils.isEmpty(viewModel.getSelectedChoice()))
                selectedChoice = viewModel.getSelectedChoice();
            else
                selectedChoice = choiceOne.getText().toString();
        } else {
            selectedChoice = savedInstanceState.getString(Constants.FRAGMENT_YOUR_FEEDS_SELECTED_CHOICE_KEY);
        }
        changeButtonBackgroundToSelected(selectedChoice, getResources().getColor(R.color.colorPrimaryLight));
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);

        if (viewModel.getmNewsFeeds() == null || viewModel.getmNewsFeeds().size() == 0) {
            makeNetworkRequestForNewsHeadlines(selectedChoice);
        } else{
            mNewsFeeds.clear();
            mNewsFeeds.addAll(viewModel.getmNewsFeeds());
            adapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.INVISIBLE);
            yourFeedsRecyclerView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void setUpViews(View view) {
        choiceOne = view.findViewById(R.id.btn_user_interest_one_frag_your_feed);
        choiceTwo = view.findViewById(R.id.btn_user_interest_two_frag_your_feed);
        choiceThree = view.findViewById(R.id.btn_user_interest_three_frag_your_feed);
        yourFeedsRecyclerView = view.findViewById(R.id.rv_news_feed_frag_your_feed);


        loadingLayout = view.findViewById(R.id.ll_loading_layout_frag);
        loadingLayoutImage = view.findViewById(R.id.img_view_error_img_frag);
        loadingBar = view.findViewById(R.id.prg_bar_frag);
        loadingLayoutMessage = view.findViewById(R.id.tv_message_ll_frag);
        retryLoadingButton = view.findViewById(R.id.btn_retry_loading_frag);

        retryLoadingButton.setOnClickListener(this);

        setLoadingLayoutVisibility();

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

        yourFeedsRecyclerView.setAdapter(adapter);
        yourFeedsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                                                LinearLayoutManager.VERTICAL, false));

        // Populate the Choices button with the appropriate choice text by fetching it from
        // Shared Preference
        ArrayList<String> userInterest = Utility.getUserInterests(getActivity());
        if (!userInterest.isEmpty()) {
            choiceOne.setText(userInterest.get(0));
            choiceTwo.setText(userInterest.get(1));
            choiceThree.setText(userInterest.get(2));
        } else {
            Log.d(YourFeedFragment.class.getSimpleName(), "Error getting user interests from Shared Preferences");
        }

        // Setting OnClickListeners for the buttons to display the corresponding data
        choiceOne.setOnClickListener(this);
        choiceTwo.setOnClickListener(this);
        choiceThree.setOnClickListener(this);
    }

    private void setLoadingLayoutVisibility() {
        retryLoadingButton.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.VISIBLE);
        loadingLayoutImage.setVisibility(View.GONE);
        loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
        yourFeedsRecyclerView.setVisibility(View.GONE);
    }

    private void changeButtonBackgroundToSelected(String selectedChoice, int colorCode) {
        if (selectedChoice.equals(choiceOne.getText().toString())) {
            choiceOne.setBackgroundColor(colorCode);
        }else if (selectedChoice.equals(choiceTwo.getText().toString())) {
            choiceTwo.setBackgroundColor(colorCode);
        } else {
            choiceThree.setBackgroundColor(colorCode);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_user_interest_one_frag_your_feed:
                if (!selectedChoice.equals(choiceOne.getText().toString())) {
                    changeButtonBackgroundToSelected(selectedChoice, getResources().getColor(R.color.colorPrimary));
                    selectedChoice = choiceOne.getText().toString();
                    setLoadingLayoutVisibility();
                    makeNetworkRequestForNewsHeadlines(selectedChoice);
                    changeButtonBackgroundToSelected(selectedChoice, getResources().getColor(R.color.colorPrimaryLight));
                }
                break;
            case R.id.btn_user_interest_two_frag_your_feed:
                if (!selectedChoice.equals(choiceTwo.getText().toString())) {
                    changeButtonBackgroundToSelected(selectedChoice, getResources().getColor(R.color.colorPrimary));
                    selectedChoice = choiceTwo.getText().toString();
                    setLoadingLayoutVisibility();
                    makeNetworkRequestForNewsHeadlines(selectedChoice);
                    changeButtonBackgroundToSelected(selectedChoice, getResources().getColor(R.color.colorPrimaryLight));
                }
                break;
            case R.id.btn_user_interest_three_frag_your_feed:
                if (!selectedChoice.equals(choiceThree.getText().toString())) {
                    changeButtonBackgroundToSelected(selectedChoice, getResources().getColor(R.color.colorPrimary));
                    selectedChoice = choiceThree.getText().toString();
                    setLoadingLayoutVisibility();
                    makeNetworkRequestForNewsHeadlines(selectedChoice);
                    changeButtonBackgroundToSelected(selectedChoice, getResources().getColor(R.color.colorPrimaryLight));
                }
                break;
            case R.id.btn_retry_loading_frag:
                loadingLayoutImage.setVisibility(View.GONE);
                loadingBar.setVisibility(View.VISIBLE);
                retryLoadingButton.setVisibility(View.GONE);
                loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
                makeNetworkRequestForNewsHeadlines(selectedChoice);
                break;
        }
        viewModel.setSelectedChoice(selectedChoice);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.FRAGMENT_YOUR_FEEDS_SELECTED_CHOICE_KEY, selectedChoice);
    }

    private void makeNetworkRequestForNewsHeadlines(String selectedChoice) {

        apiInterface.getNewsHeadlineOfQuery(selectedChoice, Constants.API_KEY,
                Constants.LANGUAGE_ENGLISH, null, null).enqueue(new Callback<NewsHeadlineResponse>() {
            @Override
            public void onResponse(Call<NewsHeadlineResponse> call, Response<NewsHeadlineResponse> response) {
                if (response.isSuccessful()) {
                    // First clearing all the previous data
                    mNewsFeeds.clear();
                    ArrayList<NewsFeedModel> feeds = new ArrayList<>();
                    loadingLayout.setVisibility(View.INVISIBLE);
                    yourFeedsRecyclerView.setVisibility(View.VISIBLE);
                    for (Articles article: response.body().getArticles()) {
                        feeds.add(new NewsFeedModel(article.getTitle()
                                ,article.getPublishedAt(), article.getUrlToImage(),
                                article.getAuthor(), article.getContent(),
                                article.getSource().getName(), article.getUrl()));
                    }
                    viewModel.setmNewsFeeds(feeds);
                    mNewsFeeds.addAll(viewModel.getmNewsFeeds());
                    adapter.notifyDataSetChanged();
                } else {
                    displayRetryLayout();
                }
            }

            @Override
            public void onFailure(Call<NewsHeadlineResponse> call, Throwable t) {
                displayRetryLayout();
            }
        });
    }

    private void displayRetryLayout() {
        retryLoadingButton.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.GONE);
        loadingLayoutImage.setVisibility(View.VISIBLE);
        loadingLayoutMessage.setText(getResources().getString(R.string.error_loading_feeds));
    }
}
