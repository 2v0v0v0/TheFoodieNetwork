package com.fbu.thefoodienetwork.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;
import com.fbu.thefoodienetwork.databinding.ActivityRestaurantDetailsBinding;
import com.fbu.thefoodienetwork.models.Restaurant;

import org.parceler.Parcels;

public class RestaurantDetailsActivity extends AppCompatActivity {
    private final static String TAG = "RestaurantDetails";
    private ActivityRestaurantDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Restaurant restaurant = (Restaurant) Parcels.unwrap(getIntent().getParcelableExtra("selectedRestaurant"));
        Log.i(TAG , restaurant.toString());
    }
}