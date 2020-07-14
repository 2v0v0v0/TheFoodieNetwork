package com.fbu.thefoodienetwork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.ZomatoRequest;
import com.fbu.thefoodienetwork.models.Location;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZomatoRequest zq = new ZomatoRequest();
        List<Location> locations = zq.getLocations("Lawrenceville");
        Location l = locations.get(4);
        zq.getRetaurants(l, "Korean", 0, 20);
    }


}