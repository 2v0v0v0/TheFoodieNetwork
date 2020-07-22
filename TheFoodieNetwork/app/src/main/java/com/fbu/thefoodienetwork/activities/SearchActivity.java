package com.fbu.thefoodienetwork.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.thefoodienetwork.API_Severs.ZomatoRequest;
import com.fbu.thefoodienetwork.adapters.LocationAdapter;
import com.fbu.thefoodienetwork.adapters.RestaurantAdapter;
import com.fbu.thefoodienetwork.databinding.ActivitySearchBinding;
import com.fbu.thefoodienetwork.models.Location;
import com.fbu.thefoodienetwork.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LocationAdapter.OnClickLocationListener, RestaurantAdapter.OnClickRestaurantListener {
    private static final String TAG = "SearchActivity";
    private static final int REQUEST_LOCATION = 100;
    private static LocationManager locationManager;
    private ActivitySearchBinding binding;
    private ZomatoRequest zomatoRequest = new ZomatoRequest();
    private List<Location> locationList = new ArrayList<>();
    private List<Restaurant> restaurantList = new ArrayList<>();
    private LocationAdapter locationAdapter;
    private RestaurantAdapter restaurantAdapter;
    private Location selectedLocation;
    private Restaurant selectedRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        searchListener();
        binding.resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void searchListener() {
        binding.getMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocationByLatLon();
                }
            }
        });

        binding.locationSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWord = binding.locationSearch.getText().toString();
                    Log.i(TAG, keyWord);
                    performLocationSearch(keyWord);
                    return true;
                }
                return false;
            }
        });

        binding.restaurantSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (selectedLocation == null) {
                        binding.locationSearch.setError("Select a location first.");
                        return true;
                    }
                    String keyWord = binding.restaurantSearch.getText().toString();
                    Log.i(TAG, keyWord);
                    performRestaurantSearch(selectedLocation, keyWord);
                    return true;
                }
                return false;
            }
        });

        binding.locationResultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.locationResultTextView.setVisibility(View.GONE);
                binding.locationSearch.setVisibility(View.VISIBLE);
                selectedLocation = null;
            }
        });
    }

    private void composeButtonListener() {
        binding.composeFltButton.setVisibility(View.VISIBLE);
        binding.composeFltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToComposeFragment();
            }
        });
    }

    @Override
    public void onClickLocation(int position) {
        selectedLocation = locationList.get(position);
        String locationTitle = selectedLocation.getTitle();
        //show selected location as text view
        binding.locationSearch.getText().clear();
        binding.locationResultTextView.setText(locationTitle);
        binding.locationSearch.setVisibility(View.GONE);
        binding.locationResultTextView.setVisibility(View.VISIBLE);
        //clear recycler view
        Log.i(TAG, "selected: " + locationTitle);
        locationList.clear();
        locationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRestaurant(int position) {
        selectedRestaurant = restaurantList.get(position);
        String restaurantName = selectedRestaurant.getName();
        Log.i(TAG, "selected: " + restaurantName);
    }

    @Override
    public void onClickMoreInfo(boolean indicator) {
        if (indicator == true) {
            goToRestaurantDetailsActivity();
            return;
        }
    }

    private void performLocationSearch(String keyWord) {
        this.locationList = zomatoRequest.getLocations(keyWord);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, locationList.toString());
                locationAdapter = new LocationAdapter(SearchActivity.this, locationList);
                binding.resultsRecyclerView.setAdapter(locationAdapter);
            }
        }, 2000);
    }

    private void performRestaurantSearch(Location location, String keyWord) {
        this.restaurantList = zomatoRequest.getRetaurants(location, keyWord, 0, 20);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, restaurantList.toString());
                restaurantAdapter = new RestaurantAdapter(SearchActivity.this, restaurantList);
                binding.resultsRecyclerView.setAdapter(restaurantAdapter);
                selectedRestaurant = restaurantList.get(0);//set first result as default value
                composeButtonListener();
            }
        }, 2000);
    }

    private void getLocationByLatLon() {
        if (ActivityCompat.checkSelfPermission(
                SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            android.location.Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double lon = locationGPS.getLongitude();
                Log.i(TAG,"lat: " + lat + " lon: " + lon);
                Toast.makeText(this, "lat: " + lat + " lon: " + lon, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToRestaurantDetailsActivity() {
        Intent intent = new Intent(this, RestaurantDetailsActivity.class);
        intent.putExtra("selectedRestaurant", Parcels.wrap(selectedRestaurant));
        startActivity(intent);
    }

    private void goToComposeFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selectedRestaurant", Parcels.wrap(selectedRestaurant));
        setResult(RESULT_OK, intent);
        finish();
    }
}