package com.fbu.thefoodienetwork.models;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fbu.thefoodienetwork.CurrentUserUtilities;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcel;

import java.util.Date;

@ParseClassName("Review")
@Parcel(analyze = ParseReview.class)
public class ParseReview extends ParseObject {
    private static final String TAG = "ParseReview";
    public static final String CREATED_AT_KEY = "createdAt";
    public static final String AUTHOR_KEY = "author";
    public static final String RESTAURANT_KEY = "restaurant";
    public static final String RATING_KEY = "rating";
    public static final String GLOBAL_KEY = "isGlobal";
    public static final String TEXT_KEY = "reviewText";
    public static final String RECOMMEND_KEY = "recommend";
    public static final String HEART_COUNT_KEY = "heartCount";
    public static final String HEART_RELATION_KEY = "heart";
    public boolean isBookmarked = false;
    public boolean isHearted = false;
    public int heartCount = 0;
    private ParseRelation heartRelation;

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

    public Date getTime() {
        return getCreatedAt();
    }

    public boolean getBookmark() {
        return isBookmarked;
    }

    public void setBookmark(boolean mark) {
        isBookmarked = mark;
    }

    public void increaseHeart() {
        heartCount += 1;
        isHearted = true;
        heartRelation.add(CurrentUserUtilities.getInstance().getCurrentUser());
        setHeartCount();
    }

    public void decreaseHeart() {
        heartCount -= 1;
        isHearted = false;
        heartRelation.remove(CurrentUserUtilities.getInstance().getCurrentUser());
        setHeartCount();
    }

    public void setHeartCount(){
        put(HEART_COUNT_KEY, heartCount);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error" +e);
                    return;
                }
                Log.i(TAG, "success saving");
            }
        });
    }

    public int getHeartCount() {
        heartCount = getInt(HEART_COUNT_KEY);
        return heartCount;
    }

    public boolean isHearted(){
        return isHearted;
    }

    public void getHeartRelation() {
        heartRelation = this.getRelation(HEART_RELATION_KEY);
        ParseQuery query = heartRelation.getQuery();

        query.equals(ParseUser.getCurrentUser());

        try {
            ParseUser user = (ParseUser) query.getFirst();
            if (user != null){
                isHearted = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        ParseReview other = (ParseReview) obj;

        if (other.getObjectId().equals(this.getObjectId())) {
            return true;
        }

        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getObjectId();
    }
}
