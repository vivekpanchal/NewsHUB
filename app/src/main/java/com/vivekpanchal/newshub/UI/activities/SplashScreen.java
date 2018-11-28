package com.vivekpanchal.newshub.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.vivekpanchal.newshub.R;
import com.vivekpanchal.newshub.services.NewsUpdateService;
import com.vivekpanchal.newshub.utility.Utility;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        // Reloading the Widget to show the updated news_placeholder every time the app is opened
        NewsUpdateService.startActionNewsWidgetUpdate(this);

        //region To check if its First Launch then ReDirect the user to UserIntrestActivity so that he can Select the Favs Categories

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utility.getIsFirstLaunch(SplashScreen.this)) {
                    Utility.setIsFirstLaunch(SplashScreen.this, false);
                    Intent i = new Intent(SplashScreen.this, UsersInterestsActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashScreen.this, NewsDisplayActivity.class);
                    startActivity(i);
                }
            }
        }, 1000);

        //endregion

    }
}
