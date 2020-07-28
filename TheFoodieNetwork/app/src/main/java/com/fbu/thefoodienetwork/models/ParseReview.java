package com.fbu.thefoodienetwork.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;

@ParseClassName("Review")
@Parcel(analyze = ParseReview.class)
public class ParseReview extends ParseObject {
    public static final String CREATED_AT_KEY = "createdAt";
    public static final String AUTHOR_KEY = "author";
    public static final String RESTAURANT_KEY = "restaurant";
    public static final String RATING_KEY = "rating";
    public static final String GLOBAL_KEY = "isGlobal";
    public static final String TEXT_KEY = "reviewText";
    public static final String RECOMMEND_KEY = "recommend";

    public ParseReview() {
    }

    public ParseUser getAuthor() {
        return getParseUser(AUTHOR_KEY);
    }

    public void setAuthor(ParseUser author) {
        put(AUTHOR_KEY, author);
    }

    public ParseRestaurant getRestaurant() {
        return (ParseRestaurant) getParseObject(RESTAURANT_KEY);
    }

    public void setRestaurant(ParseRestaurant restaurant) {
        put(RESTAURANT_KEY, restaurant);
    }

    public float getRating() {
        return (float) getDouble(RATING_KEY);
    }

    public void setRating(float rating) {
        put(RATING_KEY, rating);
    }

    public boolean getRecommend() {
        return getBoolean(RECOMMEND_KEY);
    }

    public void setRecommend(boolean recommend) {
        put(RECOMMEND_KEY, recommend);
    }

    public String getText() {
        return getString(TEXT_KEY);
    }

    public void setText(String text) {
        put(TEXT_KEY, text);
    }

    public boolean getGlobal() {
        return getBoolean(GLOBAL_KEY);
    }

    public void setGlobal(boolean shareWithEveryone) {
        put(GLOBAL_KEY, shareWithEveryone);
    }

    public Date getCreatedAt() {
        return getDate(CREATED_AT_KEY);
    }
}
