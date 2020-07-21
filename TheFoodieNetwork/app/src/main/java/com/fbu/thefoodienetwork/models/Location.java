package com.fbu.thefoodienetwork.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Location {
    private String entity_type;
    private int entity_id;
    private String title;
    private int city_id;
    private double latitude;
    private double longitude;
    private String address;

    public Location() {
    } // empty constructor needed by the Parceler library

    public Location(String latitude, String longitude, String address) {
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.address = address;
    }

    public Location(JSONObject jsonObject) throws JSONException {
        entity_type = jsonObject.getString("entity_type");
        entity_id = jsonObject.getInt("entity_id");
        title = jsonObject.getString("title");
        latitude = jsonObject.getDouble("latitude");
        longitude = jsonObject.getDouble("longitude");
    }

    public static List<Location> fromJsonArray(JSONArray locationJsonArray) throws JSONException {
        List<Location> locationList = new ArrayList<>();
        for (int i = 0; i < locationJsonArray.length(); i++) {
            locationList.add(new Location(locationJsonArray.getJSONObject(i)));
        }
        return locationList;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "entity_type='" + entity_type + '\'' +
                ", entity_id=" + entity_id +
                ", title='" + title + '\'' +
                ", city_id=" + city_id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }
}

