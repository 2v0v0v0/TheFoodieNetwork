package com.fbu.thefoodienetwork.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fbu.thefoodienetwork.databinding.ActivityBookmarkBinding;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    private final static String TAG = "BookmarkActivity";
    private ActivityBookmarkBinding binding;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private List<ParseReview> bookmarkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


    }

    private void queryBookmarks(){
        ParseRelation relation = currentUser.getRelation("bookmarked");
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseReview>() {
            public void done(List<ParseReview> results, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                } else {
                    for (ParseReview review : results) {
                        Log.i("bookmark", review.getText());
                        bookmarkList.add(review);
                    }
                }
            }
        });
    }

}