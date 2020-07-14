package com.fbu.thefoodienetwork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.fbu.thefoodienetwork.ZomatoRequest;
import com.fbu.thefoodienetwork.databinding.ActivityMainBinding;
import com.fbu.thefoodienetwork.databinding.ActivitySearchBinding;
import com.fbu.thefoodienetwork.models.Location;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private ActivitySearchBinding binding;
    private ZomatoRequest zomatoRequest = new ZomatoRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        searchListener();
    }

    private void searchListener(){
        binding.locationSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWord = binding.locationSearch.getText().toString();
                    performSearchLocation(keyWord);
                    Toast.makeText(SearchActivity.this, binding.locationSearch.getText(), Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
        binding.restaurantSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(SearchActivity.this, binding.restaurantSearch.getText(), Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }

    private List<Location> performSearchLocation(String keyWord){
        List<Location> locationList = new ArrayList<>();
        locationList = zomatoRequest.getLocations(keyWord);
        Log.i(TAG, locationList.toString());
        return locationList;
    }
}