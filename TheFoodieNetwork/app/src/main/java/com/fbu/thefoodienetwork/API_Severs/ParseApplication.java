package com.fbu.thefoodienetwork.API_Severs;

import android.app.Application;

import com.fbu.thefoodienetwork.BuildConfig;
import com.fbu.thefoodienetwork.models.ParseRestaurant;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    private static final String clientKey = BuildConfig.HEROKU_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ParseRestaurant.class);
        ParseObject.registerSubclass(ParseReview.class);

        // Initalize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("the-foodie-network")
                .clientKey(clientKey)
                .server("https://the-foodie-network.herokuapp.com/parse").build());
    }
}
