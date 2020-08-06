package com.fbu.thefoodienetwork.apiservers;

import android.util.Log;

import com.fbu.thefoodienetwork.BuildConfig;
import com.fbu.thefoodienetwork.keys.ZomatoKeys;
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
    private static final String API_KEY = BuildConfig.ZOMATO_KEY;
    private static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";

    //max number of results to display
    private static final String MAX_RESULTS = "20";


    private OkHttpClient client = new OkHttpClient();
    private HttpUrl.Builder urlBuilder;

    public List<Location> getLocations(String query) {
        final List<Location> locationList = new ArrayList<>();
        urlBuilder = HttpUrl.parse(BASE_URL + LOCATIONS).newBuilder();
        urlBuilder.addQueryParameter(ZomatoKeys.API_KEY, API_KEY);
        urlBuilder.addQueryParameter(ZomatoKeys.COUNT, "10"); ////max number of results to display
        urlBuilder.addQueryParameter(ZomatoKeys.QUERY, query); //search keyword
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONArray locations = json.getJSONArray(ZomatoKeys.LOCATION_SUGGESTIONS);
                        locationList.addAll(Location.fromJsonArray(locations));
                        Log.i(TAG, locationList.toString());
                    } catch (JSONException e) {
                        Log.i(TAG, "error: " + e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFaliure");
            }
        });
        return locationList;
    }

    public void getLocationByGeoPoint(double lat, double lon, final GeoLocationCallbacks callbacks) {
        final Location[] location = new Location[1];
        urlBuilder = HttpUrl.parse(BASE_URL + GEO_CODE).newBuilder();
        urlBuilder.addQueryParameter(ZomatoKeys.API_KEY, API_KEY);
        urlBuilder.addQueryParameter(ZomatoKeys.LAT, String.valueOf(lat));
        urlBuilder.addQueryParameter(ZomatoKeys.LON, String.valueOf(lon));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONObject locationJSONObject = json.getJSONObject(ZomatoKeys.LOCATION);
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
                Log.i(TAG, "onFailure: " + e);
                callbacks.onFailure(e);
            }
        });
    }

    public List<Restaurant> getRestaurants(Location location, String query, int start) {
        //Sprint3Res
        final List<Restaurant> restaurantList = new ArrayList<>();

        urlBuilder = HttpUrl.parse(BASE_URL + SEARCH).newBuilder();

        urlBuilder.addQueryParameter(ZomatoKeys.API_KEY, API_KEY);
        urlBuilder.addQueryParameter(ZomatoKeys.LAT, String.valueOf(location.getLatitude()));
        urlBuilder.addQueryParameter(ZomatoKeys.LON, String.valueOf(location.getLongitude()));

        urlBuilder.addQueryParameter(ZomatoKeys.SORT, ZomatoKeys.REAL_DISTANCE); // sort results by distance
        urlBuilder.addQueryParameter(ZomatoKeys.Q, query); //search keyword
        urlBuilder.addQueryParameter(ZomatoKeys.START, String.valueOf(start)); //fetch results after offset
        urlBuilder.addQueryParameter(ZomatoKeys.COUNT, MAX_RESULTS); //max number of results to display

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
                    int numOfResults = json.getInt(ZomatoKeys.RESULTS_FOUND);
                    JSONArray restaurants = json.getJSONArray(ZomatoKeys.RESTAURANTS);
                    restaurantList.addAll(Restaurant.fromJsonArray(restaurants));

                    Log.i(TAG, restaurants.toString());
                    Log.i(TAG, restaurantList.toString());
                    Log.i(TAG, "results found: " + numOfResults);
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

    public void getRestaurantUrl(int id, final RestaurantUrlCallBacks callbacks){

        urlBuilder = HttpUrl.parse(BASE_URL + RESTAURANT).newBuilder();
        urlBuilder.addQueryParameter(ZomatoKeys.API_KEY, API_KEY);
        urlBuilder.addQueryParameter(ZomatoKeys.RES_ID, String.valueOf(id));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callbacks.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        String resUrl = json.getString(ZomatoKeys.URL);
                        callbacks.onSuccess(resUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    public interface GeoLocationCallbacks {
        void onSuccess(Location location);

        void onFailure(IOException e);
    }

    public interface RestaurantUrlCallBacks{
        void onSuccess(String url);

        void onFailure(IOException e);
    }

}
