package com.fbu.thefoodienetwork.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.parceler.Parcel;

@ParseClassName("Restaurant")
@Parcel(analyze = ParseRestaurant.class)
public class ParseRestaurant extends ParseObject {
    public static final String ID_KEY = "objectId";
    public static final String ZOMATO_ID_KEY = "zomatoID";
    public static final String NAME_KEY = "name";
    public static final String CUISINES_KEY = "cuisines";
    public static final String ADDRESS_KEY = "address";
    public static final String GEO_POINT_KEY = "geoPoint";
    private Restaurant restaurant;

    public ParseRestaurant() {
    }

    public ParseRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void set() {
        put(ZOMATO_ID_KEY, restaurant.getId());
        put(NAME_KEY, restaurant.getName());
        put(CUISINES_KEY, restaurant.getCuisines());
        put(ADDRESS_KEY, restaurant.getAddress());
        ParseGeoPoint point = new ParseGeoPoint(restaurant.getLat(), restaurant.getLon());
        put(GEO_POINT_KEY, point);
    }

    public String getParseId() {
        return getString(ID_KEY);
    }

    public int getZomatoId() {
        return getInt(ZOMATO_ID_KEY);
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public String getCuisines() {
        return getString(CUISINES_KEY);
    }

    public String getAddress() {
        return getString(ADDRESS_KEY);
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint(GEO_POINT_KEY);
    }
}
