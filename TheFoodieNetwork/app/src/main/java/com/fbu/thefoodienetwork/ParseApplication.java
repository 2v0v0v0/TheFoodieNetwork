package com.fbu.thefoodienetwork;

import android.app.Application;

import com.fbu.thefoodienetwork.models.ParseRestaurant;
import com.fbu.thefoodienetwork.models.Restaurant;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    private static final String clientKey = BuildConfig.HEROKU_KEY;
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ParseRestaurant.class);
        // Initalize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("the-foodie-network")
                .clientKey(clientKey)
                .server("https://the-foodie-network.herokuapp.com/parse").build());
    }
}
