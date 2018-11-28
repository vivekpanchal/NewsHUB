package com.vivekpanchal.newshub.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.vivekpanchal.newshub.models.Articles;
import com.vivekpanchal.newshub.models.NewsHeadlineResponse;
import com.vivekpanchal.newshub.networking.APIClient;
import com.vivekpanchal.newshub.utility.Constants;
import com.vivekpanchal.newshub.widget.NewsWidgetProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NewsUpdateService extends IntentService {

    public static final String ACTION_NEWS_WIDGET_UPDATE = "com.developersudhanshu.newsdash.action.news_widget_update";
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private static final String ERROR_IMAGE_URL = "https://hostenko.com/wpcafe/wp-content/uploads/error_news_image-404-thumbnail.png";

    public NewsUpdateService() {
        super("NewsUpdateService");
    }

    public static void startActionNewsWidgetUpdate(Context context) {
        Intent i = new Intent(context, NewsUpdateService.class);
        i.setAction(ACTION_NEWS_WIDGET_UPDATE);
        context.startService(i);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_NEWS_WIDGET_UPDATE)) {
                updateNewsWidget();
            }
        }
    }

    private void updateNewsWidget() {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        final int appIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewsWidgetProvider.class));

        String result = null;
        String inputLine;

        try {
            String urlString = APIClient.BASE_URL + Constants.TOP_HEADLINES_API_REQUEST + "?" + Constants.QUERY_PARAM_API_KEY + "=" + Constants.API_KEY + "&" + Constants.QUERY_PARAM_LANGUAGE + "=" + Constants.LANGUAGE_ENGLISH;
            URL myUrl = new URL(urlString);
            //Create a connection
            HttpsURLConnection connection;

            connection = (HttpsURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(result)) {
            Gson gson = new Gson();
            NewsHeadlineResponse response = gson.fromJson(result, NewsHeadlineResponse.class);
            Articles article = response.getArticles()[0];
            NewsWidgetProvider.updateNewsWidgets(this, appWidgetManager, appIds,
                    article.getTitle(), article.getPublishedAt(),
                    article.getUrlToImage());
        } else {
            NewsWidgetProvider.updateNewsWidgets(this, appWidgetManager, appIds,
                    "Error occurred", "Error occurred",
                    ERROR_IMAGE_URL);
        }
    }
}
