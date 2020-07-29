package com.fbu.thefoodienetwork.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.adapters.ReviewAdapter;
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
    public static List<ParseReview> bookmarkList = new ArrayList<>();
    private static ParseUser currentUser = ParseUser.getCurrentUser();
    private ActivityBookmarkBinding binding;
    private ReviewAdapter reviewAdapter;
    private RecyclerView reviewRecyclerView;

    public void queryBookmarks() {
        ParseRelation relation = currentUser.getRelation("bookmarked");
        ParseQuery query = relation.getQuery();
        query.include(ParseReview.AUTHOR_KEY);
        query.include(ParseReview.RESTAURANT_KEY);

        query.findInBackground(new FindCallback<ParseReview>() {
            public void done(List<ParseReview> results, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                } else {
                    for (ParseReview review : results) {
                        Log.i(TAG, review.getObjectId());
                        review.setBookmark(true);
                    }

                    bookmarkList.addAll(results);
                    reviewAdapter.notifyDataSetChanged();
                    Log.i(TAG, bookmarkList.toString());

                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        queryBookmarks();

        reviewRecyclerView = binding.bookmarkRecyclerView;

        reviewAdapter = new ReviewAdapter(BookmarkActivity.this, bookmarkList);
        reviewRecyclerView.setAdapter(reviewAdapter);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(BookmarkActivity.this));

    }

}