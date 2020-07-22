package com.fbu.thefoodienetwork.API_Severs;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fbu.thefoodienetwork.BuildConfig;
import com.fbu.thefoodienetwork.models.Location;
import com.fbu.thefoodienetwork.models.Restaurant;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ZomatoRequest {
    //endpoints
    public static final String LOCATIONS = "locations";
    public static final String LOCATION_DETAILS = "location_details";
    public static final String SEARCH = "search";
    public static final String GEO_CODE = "geocode";
    public static final String RESTAURANT = "restaurant";
    private static final String TAG = "ZomatoRequest";
    private static final String apiKey = BuildConfig.ZOMATO_KEY;
    private static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";
    private OkHttpClient client = new OkHttpClient();
    private HttpUrl.Builder urlBuilder;

    public List<Location> getLocations(String query) {
        final List<Location> locationList = new ArrayList<>();
        urlBuilder = HttpUrl.parse(BASE_URL + LOCATIONS).newBuilder();
        urlBuilder.addQueryParameter("apikey", apiKey);
        urlBuilder.addQueryParameter("count", "10"); ////max number of results to display
        urlBuilder.addQueryParameter("query", query); //search keyword
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG, "onResponse");
                String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    JSONArray locations = json.getJSONArray("location_suggestions");
                    locationList.addAll(Location.fromJsonArray(locations));
                    Log.i(TAG, locationList.toString());
                } catch (JSONException e) {
                    Log.i(TAG, "error: " + e);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFaliure");
            }
        });
        return locationList;
    }

    public Location getLocationByGeoPoint(double lat, double lon, final geoLocationCallbacks callbacks){
        final Location[] location = new Location[1];
        urlBuilder = HttpUrl.parse(BASE_URL + GEO_CODE).newBuilder();
        urlBuilder.addQueryParameter("apikey", apiKey);
        urlBuilder.addQueryParameter("lat", String.valueOf(lat));
        urlBuilder.addQueryParameter("lon", String.valueOf(lon));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONObject locationJSONObject = json.getJSONObject("location");
                        location[0] = new Location(locationJSONObject);
                        callbacks.onSuccess(location[0]);
                        Log.i(TAG, location[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //TODO
                Log.i(TAG, "onFailure");
            }
        });

        return location[0];
    }

    public interface geoLocationCallbacks {
        void onSuccess(Location location);
        void onFailure();
    }


    public List<Restaurant> getRestaurants(Location location, String query, int start, int count) {
        final List<Restaurant> restaurantList = new ArrayList<>();
        urlBuilder = HttpUrl.parse(BASE_URL + SEARCH).newBuilder();
        urlBuilder.addQueryParameter("apikey", apiKey);
        urlBuilder.addQueryParameter("lat", String.valueOf(location.getLatitude()));
        urlBuilder.addQueryParameter("lon", String.valueOf(location.getLongitude()));
        urlBuilder.addQueryParameter("q", query); //search keyword
        urlBuilder.addQueryParameter("start", String.valueOf(start)); //fetch results after offset
        urlBuilder.addQueryParameter("count", String.valueOf(count)); //max number of results to display

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG, "onResponse");
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    JSONArray restaurants = json.getJSONArray("restaurants");
                    restaurantList.addAll(Restaurant.fromJsonArray(restaurants));
                    Log.i(TAG, restaurants.toString());
                    Log.i(TAG, restaurantList.toString());
                } catch (JSONException e) {
                    Log.i(TAG, "error: " + e);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFaliure");
            }
        });
        return restaurantList;
    }

}
