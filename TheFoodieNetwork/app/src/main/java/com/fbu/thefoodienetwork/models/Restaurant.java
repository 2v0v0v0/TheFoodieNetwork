package com.fbu.thefoodienetwork.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private String cuisines;
    private Double lat;
    private Double lon;
    private String address;

    public Restaurant(JSONObject jsonObject) throws JSONException {
        JSONObject restaurantJSONObject = jsonObject.getJSONObject("restaurant");
        JSONObject rJSONObject = restaurantJSONObject.getJSONObject("R");
        id = rJSONObject.getInt("res_id");
        name = restaurantJSONObject.getString("name");
        cuisines = restaurantJSONObject.getString("cuisines");

        JSONObject locationJSONObject = restaurantJSONObject.getJSONObject("location");
        lat = Double.parseDouble(locationJSONObject.getString("latitude"));
        lon = Double.parseDouble(locationJSONObject.getString("longitude"));
        address = locationJSONObject.getString("address");

    }

    public static List<Restaurant> fromJsonArray(JSONArray restaurantJsonArray) throws JSONException {
        List<Restaurant> restaurantList = new ArrayList<>();
        for (int i = 0; i < restaurantJsonArray.length(); i++) {
            restaurantList.add(new Restaurant(restaurantJsonArray.getJSONObject(i)));
        }
        return restaurantList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cuisines='" + cuisines + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
