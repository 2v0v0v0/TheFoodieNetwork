package com.fbu.thefoodienetwork.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Restaurant")
public class ParseRestaurant extends ParseObject {
    private Restaurant restaurant;
    public static final String ID = "objectId";
    public static final String ZOMATO_ID = "zomatoID";
    public static final String NAME = "name";
    public static final String CUISINES = "cuisines";
    public static final String ADRESS = "address";
    public static final String GEO_POINT = "geoPoint";

    public ParseRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ParseRestaurant(){};

    public void set(){
        put(ZOMATO_ID, restaurant.getId());
        put(NAME, restaurant.getName());
        put(CUISINES, restaurant.getCuisines());
        put(ADRESS, restaurant.getAddress());
        ParseGeoPoint point = new ParseGeoPoint(restaurant.getLat(), restaurant.getLon());
        put(GEO_POINT, point);
    }

    public String getParseId() {
        return getString(ID);
    }

    public int getZomatoId() {
        return getInt(ZOMATO_ID);
    }

    public String getNAME() {
        return getString(NAME);
    }

    public String getCUISINES() {
        return getString(CUISINES);
    }

    public String getADRESS() {
        return getString(ADRESS);
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint(GEO_POINT);
    }
}
