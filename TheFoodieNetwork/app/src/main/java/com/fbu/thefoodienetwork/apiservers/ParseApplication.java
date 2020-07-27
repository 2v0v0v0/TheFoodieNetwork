package com.fbu.thefoodienetwork.apiservers;

import android.app.Application;

import com.fbu.thefoodienetwork.BuildConfig;
import com.fbu.thefoodienetwork.models.ParseRestaurant;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ParseRestaurant.class);
        ParseObject.registerSubclass(ParseReview.class);

        // Initalize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("the-foodie-network")
                .clientKey(BuildConfig.HEROKU_KEY)
                .server("https://the-foodie-network.herokuapp.com/parse").build());

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
