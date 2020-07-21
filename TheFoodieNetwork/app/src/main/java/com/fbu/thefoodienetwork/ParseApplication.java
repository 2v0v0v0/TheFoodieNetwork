package com.fbu.thefoodienetwork;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initalize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("the-foodie-network")
                .clientKey("fbuTheFoodieNetwork2020")
                .server("https://the-foodie-network.herokuapp.com/parse").build());
    }
}
