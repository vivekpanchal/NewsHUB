package com.vivekpanchal.newshub.utility;

import com.vivekpanchal.newshub.BuildConfig;
import com.vivekpanchal.newshub.R;
import com.vivekpanchal.newshub.models.UserInterestModel;

import java.util.ArrayList;

public abstract class Constants {
    //News category constants
    public static final String CATEGORY_POLITICS = "Politics";
    public static final String CATEGORY_SPORTS = "Sports";
    public static final String CATEGORY_TECHNOLOGY = "Technology";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_SCIENCE = "Science";
    public static final String CATEGORY_FASHION = "Fashion";
    public static final String CATEGORY_TRAVEL = "Travel";
    public static final String CATEGORY_STARTUPS = "Start Ups";
    public static final ArrayList<UserInterestModel> userInterests = new ArrayList<UserInterestModel>(){{
        add(new UserInterestModel(CATEGORY_POLITICS, R.drawable.politics));
        add(new UserInterestModel(CATEGORY_ENTERTAINMENT, R.drawable.entertainment));
        add(new UserInterestModel(CATEGORY_FASHION, R.drawable.fashion));
        add(new UserInterestModel(CATEGORY_SCIENCE, R.drawable.science));
        add(new UserInterestModel(CATEGORY_TECHNOLOGY, R.drawable.technology));
        add(new UserInterestModel(CATEGORY_STARTUPS, R.drawable.startup));
        add(new UserInterestModel(CATEGORY_TRAVEL, R.drawable.travel));
        add(new UserInterestModel(CATEGORY_SPORTS, R.drawable.sports));
    }};

    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String COUNTRY_INDIA = "in";
    public static final String LANGUAGE_ENGLISH = "en";

    public static final String CURRENTLY_DISPLAYED_FRAGMENT_KEY = "CurrentlyDisplayedFragment";
    public static final String FRAGMENT_YOUR_FEEDS_SELECTED_CHOICE_KEY = "YourFeedsSelectedChoice";

    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    // User Interests Activity Key
    public static final String CHOICE_COUNT_TEXT_VIEW_KEY = "ChoicesCountTextViewKey";
    public static final String USER_INTERESTS_TEXT_VIEW_KEY = "UserInterestsTextViewKey";
    public static final String USER_CHOICES_KEY = "UserChoicesKey";
    public static final String RECYCLER_VIEW_STATE_KEY = "RecyclerViewStateKey";

    // Shared Preferences constants and key
    public static final String SHARED_PREFERENCE_NAME = "NewsDashSharedPreference";
    public static final String IS_FIRST_LAUNCH_KEY = "IsFirstLaunchKey";
    public static final String USER_CHOICES_ARRAY_LIST_KEY = "UserChoicesKey";

    // Intent Keys
    public static final String NEWS_FEED_INTENT_EXTRA_KEY = "NewsFeedKey";

    // API Interface constants
    public static final String TOP_HEADLINES_API_REQUEST = "top-headlines";
    public static final String EVERYTHING_API_REQUEST = "everything";
    public static final String QUERY_PARAM_QUERY = "q";
    public static final String QUERY_PARAM_COUNTRY_API = "country";
    public static final String QUERY_PARAM_API_KEY = "apiKey";
    public static final String QUERY_PARAM_LANGUAGE = "language";
    public static final String QUERY_PARAM_FROM_DATE = "from";
    public static final String QUERY_PARAM_TO_DATE = "to";

}
