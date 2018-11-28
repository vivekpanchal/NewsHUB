package com.vivekpanchal.newshub.UI.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.vivekpanchal.newshub.R;
import com.vivekpanchal.newshub.UI.fragments.FavoritesFragment;
import com.vivekpanchal.newshub.UI.fragments.SearchFragment;
import com.vivekpanchal.newshub.UI.fragments.TopHeadlinesFragment;
import com.vivekpanchal.newshub.UI.fragments.YourFeedFragment;
import com.vivekpanchal.newshub.receivers.NetworkChangeReceiver;
import com.vivekpanchal.newshub.utility.Constants;
import com.vivekpanchal.newshub.utility.Utility;

import io.fabric.sdk.android.Fabric;

public class NewsDisplayActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    private TopHeadlinesFragment topHeadlinesFragment;
    private YourFeedFragment yourFeedFragment;
    private FavoritesFragment favoritesFragment;
    private SearchFragment searchFragment;
    private static String currentlyDisplayedFragment;
    private NetworkChangeReceiver networkChangeReceiver;
    private IntentFilter networkIntentFilter;
    private static boolean wasNetworkConnected;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_top_headlines:
                    if (getSupportActionBar() != null)
                        if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_top_headlines))) {
                            getSupportActionBar().setTitle(R.string.title_top_headlines);
                            currentlyDisplayedFragment = getResources().getString(R.string.title_top_headlines);
                            setFragmentCorrespondingToStringValue(currentlyDisplayedFragment);
                        }
                    return true;
                case R.id.navigation_your_feed:
                    if (getSupportActionBar() != null)
                        if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_your_feed))) {
                            getSupportActionBar().setTitle(R.string.title_your_feed);
                            currentlyDisplayedFragment = getResources().getString(R.string.title_your_feed);
                            setFragmentCorrespondingToStringValue(currentlyDisplayedFragment);
                        }
                    return true;
                case R.id.navigation_favourites:
                    if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_favorites))) {
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(R.string.title_favorites);
                        currentlyDisplayedFragment = getResources().getString(R.string.title_favorites);
                        setFragmentCorrespondingToStringValue(currentlyDisplayedFragment);
                    }
                    return true;
                case R.id.navigation_search:
                    if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_search))) {
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(R.string.title_search);
                        currentlyDisplayedFragment = getResources().getString(R.string.title_search);
                        setFragmentCorrespondingToStringValue(currentlyDisplayedFragment);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            currentlyDisplayedFragment = getResources().getString(R.string.title_top_headlines);

            // Creating fragments using singleton design pattern
            if (topHeadlinesFragment == null) {
                topHeadlinesFragment = new TopHeadlinesFragment();
            }
            if (yourFeedFragment == null) {
                yourFeedFragment = new YourFeedFragment();
            }
            if (favoritesFragment == null) {
                favoritesFragment = new FavoritesFragment();
            }
            if (searchFragment == null) {
                searchFragment = new SearchFragment();
            }
            setFragmentCorrespondingToStringValue(currentlyDisplayedFragment);

        } else {

            if (topHeadlinesFragment == null) {
                topHeadlinesFragment = new TopHeadlinesFragment();
            }
            if (yourFeedFragment == null) {
                yourFeedFragment = new YourFeedFragment();
            }
            if (favoritesFragment == null) {
                favoritesFragment = new FavoritesFragment();
            }
            if (searchFragment == null) {
                searchFragment = new SearchFragment();
            }
            currentlyDisplayedFragment = savedInstanceState.getString(Constants.CURRENTLY_DISPLAYED_FRAGMENT_KEY);
        }

        setupNetworkStateReceiver();

        // Initialising Fabric Crashlytics in my app
        Fabric.with(this, new Crashlytics());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver, networkIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    private void setupNetworkStateReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver();

        networkIntentFilter = new IntentFilter();
        networkIntentFilter.addAction(Constants.CONNECTIVITY_ACTION);

        wasNetworkConnected = false;

        networkChangeReceiver.setNetworkStateListener(new NetworkChangeReceiver.NetworkStateListener() {
            @Override
            public void networkStateConnected(boolean status) {
                if (!status && wasNetworkConnected) {
                    if (!Utility.isInternetNotAvailableDialogCreated())
                        Utility.createInternetNotAvailableDialog(NewsDisplayActivity.this);
                    Utility.displayBottomSheetDialog();
                    wasNetworkConnected = !wasNetworkConnected;
                } else if (status) {
                    wasNetworkConnected = true;
                    Utility.dismissBottomSheetDialog();
                }
            }
        });
    }

    private void setFragmentCorrespondingToStringValue(String value) {
        if (value.equals(getResources().getString(R.string.title_top_headlines))) {
            fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                    topHeadlinesFragment).commit();
        } else if (value.equals(getResources().getString(R.string.title_your_feed))) {
            fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                    yourFeedFragment).commit();
        } else if (value.equals(getResources().getString(R.string.title_favorites))) {
            fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                    favoritesFragment).commit();
        } else if (value.equals(getResources().getString(R.string.title_search))) {
            fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                    searchFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.CURRENTLY_DISPLAYED_FRAGMENT_KEY, currentlyDisplayedFragment);
    }
}
