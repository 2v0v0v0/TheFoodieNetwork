package com.fbu.thefoodienetwork.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fbu.thefoodienetwork.OnSwipeTouchListener;
import com.fbu.thefoodienetwork.adapters.ReviewAdapter;
import com.fbu.thefoodienetwork.databinding.ActivityBookmarkBinding;
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    private final static String TAG = "BookmarkActivity";
    public static List<ParseReview> bookmarkList = new ArrayList<>();
    private static ParseUser currentUser = ParseUser.getCurrentUser();
    private static ReviewAdapter reviewAdapter;
    private static ParseRelation relation;
    private ActivityBookmarkBinding binding;
    private RecyclerView reviewRecyclerView;

    public static void queryBookmarks() {
        relation = currentUser.getRelation(UserKeys.BOOKMARK);
        ParseQuery query = relation.getQuery();
        query.include(ParseReview.AUTHOR_KEY);
        query.include(ParseReview.RESTAURANT_KEY);

        query.findInBackground(new FindCallback<ParseReview>() {
            public void done(List<ParseReview> results, ParseException e) {
                if (e != null) {
                    Log.i(TAG, "error: " + e);
                } else {
                    for (ParseReview review : results) {
                        review.setBookmark(true);
                    }

                    bookmarkList.addAll(results);
                    Log.i(TAG, bookmarkList.toString());

                }
            }
        });
    }

    public static void removeBookmark(final Context context, ParseReview review) {
        bookmarkList.remove(review);

        relation.remove(review);

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(context, "error: " + e, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addBookmark(final Context context, ParseReview review) {
        bookmarkList.add(review);

        relation.add(review);

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(context, "error: " + e, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        reviewRecyclerView = binding.bookmarkRecyclerView;

        reviewAdapter = new ReviewAdapter(BookmarkActivity.this, bookmarkList);

        reviewRecyclerView.setAdapter(reviewAdapter);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(BookmarkActivity.this));

        setSwipeListener(reviewRecyclerView);
    }

    private void setSwipeListener(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                Toast.makeText(BookmarkActivity.this, "Home", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

}