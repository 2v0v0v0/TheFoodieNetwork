package com.fbu.thefoodienetwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.adapters.ReviewAdapter;
import com.fbu.thefoodienetwork.databinding.ActivityProfileBinding;
import com.fbu.thefoodienetwork.keys.UserKeys;
import com.fbu.thefoodienetwork.models.ParseReview;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

//TODO change LoginActivity goMainActivity when finish
public class ProfileActivity extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";
    private ActivityProfileBinding binding;
    private ParseUser user = ParseUser.getCurrentUser();
    private ReviewAdapter reviewAdapter;
    private List<ParseReview> allReviews;
    private LinearLayoutManager layoutManager;
    private RecyclerView reviewRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        reviewRecyclerView = binding.postsRecyclerView;
        allReviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, allReviews);
        reviewRecyclerView.setAdapter(reviewAdapter);
        layoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(layoutManager);

        loadData();

    }

    @Override
    protected void onResume() {
        Log.i(TAG, "resume");
        loadData();
        super.onResume();
    }

    private void loadData() {
        queryReviews();

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEditProfileActivity();
            }
        });

        try {

            if (user.getString(UserKeys.SCREEN_NAME) == null || user.getString(UserKeys.SCREEN_NAME).trim().equals("")) {
                binding.textViewScreenName.setText(user.getUsername());
            } else {
                binding.textViewScreenName.setText(user.getString(UserKeys.SCREEN_NAME));
                binding.textViewUsername.setText(user.getUsername());
            }


            try {
                binding.bioTextView.setText(user.getString(UserKeys.BIO));
            } catch (Exception e) {
                Log.i(TAG, "error:" + e.toString());
            }

            ParseFile image = user.getParseFile(UserKeys.PROFILE_IMAGE);
            if (image != null) {
                Glide.with(this).load(image.getUrl()).centerCrop().circleCrop().into(binding.profileImage);
            } else {
                Glide.with(this).load(R.drawable.placeholder).circleCrop().into(binding.profileImage);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }
    }

    private void goEditProfileActivity() {
        Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(editIntent);
    }

    private void queryReviews() {
        ParseQuery<ParseReview> query = ParseQuery.getQuery(ParseReview.class);

        query.include(ParseReview.AUTHOR_KEY);
        query.include(ParseReview.RESTAURANT_KEY);
        query.whereEqualTo(ParseReview.AUTHOR_KEY, user);
        query.setLimit(10);
        query.addDescendingOrder(ParseReview.CREATED_AT_KEY);

        query.findInBackground(new FindCallback<ParseReview>() {
            @Override
            public void done(List<ParseReview> reviewList, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting reviews");
                    return;
                }

                for (ParseReview review : reviewList) {
                    Log.i(TAG, "Post: " + review.getText() + ", username: " + review.getAuthor().getUsername());
                }
                allReviews.addAll(reviewList);
                reviewAdapter.notifyDataSetChanged();
            }
        });

    }

}