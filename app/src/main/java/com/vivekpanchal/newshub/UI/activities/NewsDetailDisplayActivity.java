package com.vivekpanchal.newshub.UI.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.vivekpanchal.newshub.R;
import com.vivekpanchal.newshub.database.AppDatabase;
import com.vivekpanchal.newshub.database.AppExecutors;
import com.vivekpanchal.newshub.database.NewsHeadlineEntity;
import com.vivekpanchal.newshub.models.NewsFeedModel;
import com.vivekpanchal.newshub.utility.Constants;
import com.vivekpanchal.newshub.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_news_headline_act_news_detail_display)
    private TextView newsHeadline;
    @BindView(R.id.tv_date_act_news_detail_display)
    private TextView newsDate;
    @BindView(R.id.tv_author_act_news_detail_display)
    private TextView newsAuthor;
    @BindView(R.id.tv_source_act_news_detail_display)
    private TextView newsSource;

    @BindView(R.id.img_view_act_news_detail)
    private ImageView newsImage;
    @BindView(R.id.tv_desc_act_news_detail_display)
    private TextView newsDescription;

    @BindView(R.id.btn_share_act_news_details_display)
    private Button shareStory;
    @BindView(R.id.btn_open_in_browser_act_news_details_display)
    private Button viewFullStory;

    @BindView(R.id.btn_mark_fav_act_news_details_display)
    private Button markFavoriteStory;

    @BindView(R.id.banner_ad_act_news_detail_display)
    private AdView bannerAd;

    private String newsUrl;
    private static final String TAG = NewsDetailDisplayActivity.class.getSimpleName();
    private AppDatabase mDb;
    private AppExecutors executors;
    private NewsFeedModel model;
    private boolean newsIsFav;
    private NewsHeadlineEntity mNewsEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_display);
        ButterKnife.bind(this);
        setUpViews();

        if (getIntent() != null && getIntent().hasExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY)) {
            model = getIntent().getParcelableExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY);
            populateViewsWithData(model);
        }

        executors = AppExecutors.getInstance();

        loadNewsFeedByName();

        if (getSupportActionBar() != null) {
            // Enable back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Disable title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void loadNewsFeedByName() {
        mDb = AppDatabase.getInstance(this);

        final LiveData<NewsHeadlineEntity> news = mDb.getNewsHeadlinesDao().loadNewsByHeadline(model.getName());
        news.observe(this, new Observer<NewsHeadlineEntity>() {
            @Override
            public void onChanged(@Nullable NewsHeadlineEntity headlineEntity) {
                if (headlineEntity == null) {
                    markUnFav();
                } else {
                    markFav();
                    mNewsEntity = headlineEntity;
                }
            }
        });
    }

    private void markFav() {
        newsIsFav = true;
        final Drawable drawable = getDrawable(R.drawable.ic_favorite_24dp);
        assert drawable != null;
        drawable.setTint(getResources().getColor(android.R.color.holo_red_dark));
        markFavoriteStory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    private void markUnFav() {
        newsIsFav = false;
        final Drawable drawable = getDrawable(R.drawable.ic_favorite_24dp);
        assert drawable != null;
        drawable.setTint(getResources().getColor(android.R.color.white));
        markFavoriteStory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateViewsWithData(NewsFeedModel model) {

        newsHeadline.setText(model.getName());
        newsDate.setText(Utility.getDateInReadableFormat(model.getDate()));
        if (!TextUtils.isEmpty(model.getAuthorName())) {
            newsAuthor.setText(model.getAuthorName());
        } else {
            newsAuthor.setText(getResources().getString(R.string.no_author_message));
        }
        if (!TextUtils.isEmpty(model.getDescription())) {
            newsDescription.setText(model.getDescription());
        } else {
            newsDescription.setText(getResources().getString(R.string.no_description_message));
        }
        if (!TextUtils.isEmpty(model.getNewsSource())) {
            String sourceString = getResources().getString(R.string.added_to_fav) + model.getNewsSource();
            newsSource.setText(sourceString);
        } else {
            newsSource.setText(getResources().getString(R.string.no_source_available));
        }

        // Saving the news_placeholder URL to be shared and opened in Browser
        newsUrl = model.getNewsUrl();

        // Loading the image in the image view
        Picasso.with(this)
                .load(model.getImageUrl())
                .placeholder(R.drawable.news_placeholder)
                .placeholder(R.drawable.error_news_image)
                .into(newsImage);
    }

    private void setUpViews() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        bannerAd.loadAd(adRequest);
        markFavoriteStory.setOnClickListener(this);
        shareStory.setOnClickListener(this);
        viewFullStory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mark_fav_act_news_details_display:
                // Save the News Headline in the app Database
                executors.getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        NewsHeadlineEntity entity = new NewsHeadlineEntity(model.getName(),
                                model.getDate(), model.getImageUrl(), model.getAuthorName(),
                                model.getDescription(), model.getNewsSource(), model.getNewsUrl());
                        if (newsIsFav) {
                            mDb.getNewsHeadlinesDao().deleteNewsHeadline(mNewsEntity);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(NewsDetailDisplayActivity.this, getResources().getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            mDb.getNewsHeadlinesDao().insertNewsHeadline(entity);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(NewsDetailDisplayActivity.this, getResources().getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                break;
            case R.id.btn_share_act_news_details_display:
                if (!TextUtils.isEmpty(newsUrl)) {
                    Intent shareStoryIntent = new Intent();
                    shareStoryIntent.setType("text/plain");
                    shareStoryIntent.setAction(Intent.ACTION_SEND);
                    shareStoryIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
                    startActivity(shareStoryIntent);
                } else {
                    Log.d(TAG, "News Url is Empty");
                }
                break;
            case R.id.btn_open_in_browser_act_news_details_display:
                if (!TextUtils.isEmpty(newsUrl)) {
                    Intent openInBrowserIntent = new Intent();
                    openInBrowserIntent.setAction(Intent.ACTION_VIEW);
                    openInBrowserIntent.setData(Uri.parse(newsUrl));
                    startActivity(Intent.createChooser(openInBrowserIntent, getResources().getString(R.string.intent_action_open)));
                } else {
                    Log.d(TAG, "News Url is Empty");
                }
                break;
        }
    }
}
